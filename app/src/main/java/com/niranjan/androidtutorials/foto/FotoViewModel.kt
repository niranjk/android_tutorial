package com.niranjan.androidtutorials.foto

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.niranjan.androidtutorials.MainConstants.FOTO_EDITING_WORK_NAME
import com.niranjan.androidtutorials.MainConstants.KEY_IMAGE_URI
import com.niranjan.androidtutorials.MainConstants.MY_UNIQUE_WORK
import com.niranjan.androidtutorials.MainConstants.MY_WORK_TAG_A
import com.niranjan.androidtutorials.MainConstants.MY_WORK_TAG_B
import com.niranjan.androidtutorials.MainConstants.TAG_IMAGE_BLURRED
import com.niranjan.androidtutorials.MainConstants.TAG_OUTPUT
import com.niranjan.androidtutorials.MainConstants.WORK_REQUEST_INPUT_DATA_KEY
import com.niranjan.androidtutorials.MainConstants.WORK_REQUEST_INPUT_DATA_VALUE
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.foto.workers.BlurWorker
import com.niranjan.androidtutorials.foto.workers.CleanupWorker
import com.niranjan.androidtutorials.foto.workers.SaveImageToFileWorker
import com.niranjan.androidtutorials.foto.workers.SyncPlantsImagesWorker
import java.util.*

class FotoViewModel(application: Application) : ViewModel() {
    private var imageUri: Uri? = null
    internal var outputUri: Uri? = null

    /**
     * Create the Instance of WorkManager in ViewModel
     */
    private val workManager = WorkManager.getInstance(application)

    /**
     * Create a [WorkRequest]
     *
     * and start the work using [WorkManager.enqueue]
     */
    fun doYourSyncWork(){
        val myWorkRequest = OneTimeWorkRequest.from(SyncPlantsImagesWorker::class.java)
        workManager.enqueue(myWorkRequest)
    }


    /**
     * Adding Input Data to your WorkRequest
     */
    fun createWorkBuilder(){
        val myWorkBuilder = OneTimeWorkRequestBuilder<SyncPlantsImagesWorker>()
        val myInputData = Data.Builder().putString(
            WORK_REQUEST_INPUT_DATA_KEY,
            WORK_REQUEST_INPUT_DATA_VALUE
        ).build()
        myWorkBuilder.setInputData(
            myInputData
        )
        workManager.enqueue(myWorkBuilder.build())
    }

    /**
     * WorkInfo object contains details about the current state of a WorkRequest.
     *
     * WorkStates are: [BLOCKED], [CANCELLED], [ENQUEUED], [FAILED], [RUNNING], [SUCCEEDED]
     */
    // UNIQUE ID GENEREATED BY WORKMANAGER FOR EACH WORKREQUEST
    val myWorkRequest = OneTimeWorkRequestBuilder<SyncPlantsImagesWorker>().build()
    val myWorkRequestByTag = OneTimeWorkRequestBuilder<CleanupWorker>().addTag(MY_WORK_TAG_B).build()
    val getWorkInfoByWorkId : LiveData<WorkInfo> =
        workManager.getWorkInfoByIdLiveData(myWorkRequest.id)

    val getWorkInfoByUniqueChainName : LiveData<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData(MY_UNIQUE_WORK)

    val getWorkByTagName : LiveData<List<WorkInfo>> =
        workManager.getWorkInfosByTagLiveData(MY_WORK_TAG_A)


    // Work Infos Observer by TAG
    internal val outputWorkInfos: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosByTagLiveData(TAG_IMAGE_BLURRED)

    init {
        // This transformation makes sure that whenever the current work id changes
        // the WorkInfo, the UI is listening to changes
        imageUri = getImageUri(application.applicationContext)
    }

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.cartoon))
            .appendPath(resources.getResourceTypeName(R.drawable.cartoon))
            .appendPath(resources.getResourceEntryName(R.drawable.cartoon))
            .build()
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }

    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * @param blurlevel - the amount to blur the image
     */
    internal fun applyBlur(blurLevel: Int) {
        // Add WorkRequest to Clean up temporary images
        var continuation = workManager
            .beginUniqueWork(
                FOTO_EDITING_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )

        // Add WorkRequests to blur the image the number of times requested
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>().addTag(TAG_IMAGE_BLURRED)

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation.then(blurBuilder.build())
        }

        // Create battery not low constraint
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()

        continuation = continuation.then(save)

        // Actually start the work
        continuation.enqueue()
    }

    /**
     * Chaining Multiple Works
     * Creating Unique Work Chain running at a time.
     * If work already existing then we can REPLACE, KEEP OR APPEND
     * the current work.
     */
    fun chainingMultipleWork(){
        val workA = OneTimeWorkRequestBuilder<CleanupWorker>().addTag(MY_WORK_TAG_A).build()
        val workB = OneTimeWorkRequestBuilder<BlurWorker>().setInputData(
            Data.Builder().putString(WORK_REQUEST_INPUT_DATA_KEY, "your data").build()
        ).build()
        val workC = OneTimeWorkRequestBuilder<SaveImageToFileWorker>().build()
        val workD = OneTimeWorkRequestBuilder<SyncPlantsImagesWorker>()

        val continueWork = workManager.beginUniqueWork(
            MY_UNIQUE_WORK,   // Your Unique Work Name
            ExistingWorkPolicy.REPLACE, // REPLACE, KEEP, APPEND
            workA
        )
        // chain your works in parallel
        continueWork.then(workB)
        continueWork.then(workC)
        // you can also repeat your work in chain passing the input data
        repeat(3) { level ->
            workD.setInputData(
                Data.Builder().putInt("LEVEL", level).build()
            )
            continueWork.then(workD.build())
        }
        continueWork.enqueue() // start Work
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }

    /**
     * Cancelling WorkRequests
     */
    internal fun cancelWork(){
        workManager.cancelUniqueWork(MY_UNIQUE_WORK)

    }
    internal fun cancelAllWork(){
        workManager.cancelAllWork()
    }
}


class FotoViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FotoViewModel::class.java)){
            FotoViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}