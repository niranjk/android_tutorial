package com.niranjan.androidtutorials

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.niranjan.androidtutorials.databinding.ActivityMainBinding
import com.niranjan.androidtutorials.slot.SlotActivity

/**
 * Our MainActivity extends the AppCompatActivity and inherits the behavior from the Android Framework Activity. So we can override the lifecycle methods to our MainActivity.
 */
class MainActivity : AppCompatActivity() {
    // Activity Lifecycle - Stage Created
    // ViewBinding
    private lateinit var binding : ActivityMainBinding  // Lately initalized in onCreate function call
    // private var nullableBinding: ActivityMainBinding? = null

    private val mainAdapter = MainAdapter(DummyData.dummyList()){
        // adapter item click
        when(it){
            MainConstants.Feature.SLOT.value -> {
                //navigate to SlotActivity
                startActivity(
                    Intent(this, SlotActivity::class.java)
                )
            }
            else -> {
                // nothing
            }
        }
    }
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
            mainRv.adapter = mainAdapter
            mainRv.layoutManager = LinearLayoutManager(this@MainActivity)
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