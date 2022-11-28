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
import com.niranjan.androidtutorials.MainConstants.TAG_IMAGE_BLURRED
import com.niranjan.androidtutorials.MainConstants.TAG_OUTPUT
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.foto.workers.BlurWorker
import com.niranjan.androidtutorials.foto.workers.CleanupWorker
import com.niranjan.androidtutorials.foto.workers.SaveImageToFileWorker

class FotoViewModel(application: Application) : ViewModel() {
    private var imageUri: Uri? = null
    internal var outputUri: Uri? = null

    /**
     * Create the Instance of WorkManager in ViewModel
     */
    private val workManager = WorkManager.getInstance(application)

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

        // Create charging constraint
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
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

    internal fun cancelWork(){
        workManager.cancelUniqueWork(FOTO_EDITING_WORK_NAME)
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