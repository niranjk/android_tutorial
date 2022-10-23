package com.niranjan.androidtutorials.runner

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.ActivityRunnerBinding
import com.niranjan.androidtutorials.drawer.DrawerBaseActivity

class RunnerActivity : DrawerBaseActivity(), LocationListener{

    lateinit var runnerBinding: ActivityRunnerBinding
    lateinit var viewModel: StopWatchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runnerBinding = ActivityRunnerBinding.inflate(layoutInflater)
        setContentView(runnerBinding.root)
        allocateActivityTitle(getString(R.string.label_runner_app))
        viewModel = ViewModelProvider(this)[StopWatchViewModel::class.java]
        setupStopWatchTimer()
        setListeners()
    }

    private fun checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            RunnerLocationManager(applicationContext, lifecycleOwner = this, mLocationListener = this)
        }
    }

    override fun onLocationChanged(location: Location) {
        with(runnerBinding){
            latitudeValueTv.text = location.latitude.toString()
            longitudeValueTv.text = location.longitude.toString()
        }
    }

    private fun setupStopWatchTimer(){
        var startTime  = viewModel.getStartTime()
        if(startTime != null ) runnerBinding.chronometer.base = startTime
        else {
            startTime = SystemClock.elapsedRealtime()
            viewModel.setStartTime(startTime)
            runnerBinding.chronometer.base = startTime
        }
    }

    private fun setListeners(){
        runnerBinding.timerBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            with(runnerBinding) {
                if (isChecked) {
                    timerBtn.text = getString(R.string.label_stop_watch_stop_timer)
                    chronometer.start()
                    // Check Location Permission
                    checkLocationPermission()
                } else {
                    timerBtn.text = getString(R.string.label_stop_watch_start_timer)
                    chronometer.stop()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            RunnerLocationManager(applicationContext, lifecycleOwner = this, mLocationListener = this)
        }else {
            Toast.makeText(this, "No location permission given", Toast.LENGTH_LONG).show()
        }
    }
}