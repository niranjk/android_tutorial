package com.niranjan.androidtutorials.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.ActivityLifecycleDemoBinding

class LifecycleDemoActivity : AppCompatActivity() {

    lateinit var binding: ActivityLifecycleDemoBinding

    companion object {
        const val SAVED_INSTANCE_STATE = "SAVED_INSTANCE_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * - Activity is created and initialised here
         * - This callback should be implemented because it's called first when Activity is created
         **/
        super.onCreate(savedInstanceState)
        /** - All initial Activity setup Logic is implemented here like UI setup and binding **/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lifecycle_demo)
        /** - Saved instance state is passed in onCreate to recreate the UI*/
        binding.lifecycleDemoTv.text = savedInstanceState?.getString(SAVED_INSTANCE_STATE) ?: "State-onCreate()"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        /**
         * Android Framework provides Bundle to save the small amount of data to reconstruct
         * the layout.
         * Only minimal data like Resource ID and Text are saved in Bundle.
         * You can see that Bundle is passed on onCreate callback to recreate the UI
         */
        outState.putString(SAVED_INSTANCE_STATE, "my saved text value")
    }

    override fun onStart() {
        /**
         * Activity becomes visible to the user
         * called immediately after #onCreate()
         * or called after #onRestart() if activity previously stopped
         */
        binding.lifecycleDemoTv.text = "sate - onStart()"
        super.onStart()
    }

    override fun onResume() {
        /***
         * Activity is in resumed state.
         * Comes back to Foreground from the onPause()
         * It receives input from the user in this state.
         */
        binding.lifecycleDemoTv.text = "sate - onResume()"
        super.onResume()
    }

    override fun onPause() {
        /**
         * Activity looses focus
         * But it is still visible on the screen.
         * You should prepare to release resources in this state.
         * Don't run any long-running synchronous task here because this state is short
         */
        binding.lifecycleDemoTv.text = "sate - onPause()"
        super.onPause()
    }

    override fun onStop() {
        /**
         * Activity not visible to user.
         * Release resources which is no more needed
         * Stop refreshing UI and Stop Running Animations
         * If User navigates back then onRestart() -> onStart() -> onResume() is called
         */
        super.onStop()
    }

    override fun onRestart() {
        /**
         * If User navigates back from onStop() then onRestart() -> onStart() -> onResume() is called
         */
        super.onRestart()
    }

    override fun onDestroy() {
        /**
         * Called when App is killed.
         * Activity is finished (launched to do some action and returned a value)
         * On Configuration changed the Activity is Destroyed
         * Final cleanup of resources should be done here.
         * Note* don't save user data in this state.
         */
        super.onDestroy()
    }

}