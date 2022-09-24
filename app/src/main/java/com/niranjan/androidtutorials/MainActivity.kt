package com.niranjan.androidtutorials

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.niranjan.androidtutorials.databinding.ActivityMainBinding

/**
 * Our MainActivity extends the AppCompatActivity and inherits the behavior from the Android Framework Activity. So we can override the lifecycle methods to our MainActivity.
 */
class MainActivity : AppCompatActivity() {
    // Activity Lifecycle - Stage Created

    // ViewBinding
    private lateinit var binding : ActivityMainBinding  // Lately initalized in onCreate function call
    // private var nullableBinding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /***
         * LayoutInflater parse the layout XML file and a hierarchy of View is created to be displayed in Activity.
         * This process is called Layout Inflation.
         */
        // viewbinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            // we are using a View.OnClickListener as Single Abstract Method here
            button.setOnClickListener {
                helloTv.text = resources.getString(R.string.app_name)
                // we make a toast on button press
                Toast.makeText( it.context, getString(R.string.label_press_me_desc), Toast.LENGTH_LONG).show()
            }
        }
    }
    // Activity Lifecycle - Stage Visible
    override fun onStart() {
        super.onStart()
    }
    // Activity Lifecycle - Stage Accepting User Inputs
    override fun onResume() {
        super.onResume()
    }
    // Activity Lifecycle - Stage Paused - goto onResume() or onStart()
    override fun onPause() {
        super.onPause()
    }
    // Activity Lifecycle - Stage Not Visible
    override fun onStop() {
        super.onStop()
    }
    // Activity Lifecycle - Stage Terminated
    override fun onDestroy() {
        super.onDestroy()
    }
}