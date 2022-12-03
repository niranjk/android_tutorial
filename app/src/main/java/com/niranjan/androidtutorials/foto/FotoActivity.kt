package com.niranjan.androidtutorials.foto

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.niranjan.androidtutorials.MainConstants.KEY_IMAGE_URI
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.ActivityFotoBinding
import com.niranjan.androidtutorials.drawer.DrawerBaseActivity

class FotoActivity : DrawerBaseActivity(){

    private lateinit var binding : ActivityFotoBinding

    private val viewModel: FotoViewModel by viewModels { FotoViewModelFactory(application) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        allocateActivityTitle(getString(R.string.label_foto_app))
        setViewListeners()
        setWorkInfoObservers()
    }

    /**
     * Observer your WorkInfo and Update your UI based of the WorkInfo.State
     */
    private fun setWorkInfoObservers(){
        viewModel.getWorkInfoByWorkId.observe(this) { workInfo ->
            when (workInfo.state) {
                WorkInfo.State.SUCCEEDED -> {
                }
                WorkInfo.State.FAILED -> {
                }
                WorkInfo.State.BLOCKED-> {
                }
                WorkInfo.State.CANCELLED-> {
                }
                WorkInfo.State.ENQUEUED-> {
                }
                WorkInfo.State.RUNNING-> {
                }
            }
        }

        viewModel.getWorkByTagName.observe(this){ workInfoList ->
            // no matching work with TAG
            if (workInfoList.isNullOrEmpty()){
                return@observe
            }
            val workInfo = workInfoList[0]
            when {
                workInfo.state.isFinished -> {
                    // show Work Finished
                }
                else -> {
                    // show Work In Progress
                }
            }
        }
    }
    private fun setViewListeners(){
        with(binding){
            goButton.setOnClickListener {
                viewModel.applyBlur(blurLevel)
            }
            seeFileButton.setOnClickListener {
                viewModel.outputUri?.let { currentUri ->
                    // set the blurred image to the ImageView
                    imageView.setImageURI(viewModel.outputUri)
                    /**
                    val actionView = Intent(Intent.ACTION_VIEW, currentUri)
                    actionView.resolveActivity(packageManager)?.run {
                        startActivity(actionView)
                    }
                    */
                }
            }
            cancelButton.setOnClickListener {
                viewModel.cancelWork()
            }

            viewModel.outputWorkInfos.observe(this@FotoActivity, workInfosObserver())
        }
    }


    /**
     * WorkManager --
     */
    private fun workInfosObserver(): Observer<List<WorkInfo>> {
        return Observer { listOfWorkInfo ->

            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.

            // If there are no matching work info, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@Observer
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished) {
                showWorkFinished()

                // Normally this processing, which is not directly related to drawing views on
                // screen would be in the ViewModel. For simplicity we are keeping it here.
                val outputImageUri = workInfo.outputData.getString(KEY_IMAGE_URI)

                // If there is an output file show "See File" button
                if (!outputImageUri.isNullOrEmpty()) {
                    viewModel.setOutputUri(outputImageUri)
                    binding.seeFileButton.visibility = View.VISIBLE
                }
            } else {
                showWorkInProgress()
            }
        }
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private fun showWorkInProgress() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            goButton.visibility = View.GONE
            seeFileButton.visibility = View.GONE
        }
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private fun showWorkFinished() {
        with(binding) {
            progressBar.visibility = View.GONE
            cancelButton.visibility = View.GONE
            goButton.visibility = View.VISIBLE
        }
    }

    private val blurLevel: Int
        get() =
            when (binding.radioBlurGroup.checkedRadioButtonId) {
                R.id.radio_blur_lv_1 -> 1
                R.id.radio_blur_lv_2 -> 2
                R.id.radio_blur_lv_3 -> 3
                else -> 1
            }
}