package com.niranjan.androidtutorials

import android.content.Intent
import android.os.Bundle
import com.niranjan.androidtutorials.databinding.ActivityMainBinding
import com.niranjan.androidtutorials.drawer.DrawerBaseActivity

/**
 * Our MainActivity extends the AppCompatActivity and inherits the behavior from the Android Framework Activity. So we can override the lifecycle methods to our MainActivity.
 */
class MainActivity : DrawerBaseActivity()
{
    // Activity Lifecycle - Stage Created
    // ViewBinding
    lateinit var mainBinding : ActivityMainBinding  // Lately initalized in onCreate function call
    // private var nullableBinding: ActivityMainBinding? = null

    private val mainAdapter = MainAdapter(DummyData.dummyList()){
        // adapter item click
        when(it){
            MainConstants.Feature.SLOT.value -> {
                //navigate to SlotActivity
                startActivity(
                    Intent().setClassName(
                        BuildConfig.APPLICATION_ID,
                        BuildConfig.DF_SLOT_ACTIVITY
                    )
                )
            }
            MainConstants.Feature.PRICE.value -> {
                // navigate to PriceCalculatorActivity
                startActivity(
                    Intent().setClassName(
                        BuildConfig.APPLICATION_ID,
                        BuildConfig.DF_PRICE_ACTIVITY
                    )
                )
            }
            MainConstants.Feature.QUIZ.value -> {
                // navigate to PriceCalculatorActivity
                startActivity(
                    Intent().setClassName(
                        BuildConfig.APPLICATION_ID,
                        BuildConfig.DF_QUIZ_ACTIVITY
                    )
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
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        allocateActivityTitle(getString(R.string.app_name))
        setContentView(mainBinding.root)
    }

    /*
    // Inflate the Options Menu in the Activity.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    // Handle the Menu Item Click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_about_fragment -> {
                // Navigate to About Fragment
            }
            R.id.nav_contacts -> {
                // Navigate to Contacts Fragment
            }
        }
        return true
    }

     */
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