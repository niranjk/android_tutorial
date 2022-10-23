package com.niranjan.androidtutorials.runner

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@SuppressLint("MissingPermission")
class RunnerLocationManager(
    val mContext: Context,
    lifecycleOwner: LifecycleOwner,
    var mLocationListener: LocationListener?
) : LifecycleEventObserver{
    private lateinit var mlocationManager: LocationManager
    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun addLocationListener(){
        mlocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationListener?.let {
            mlocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0L, 0f, it
            )
        }
        val lastLocation = mlocationManager.getLastKnownLocation(
            LocationManager.GPS_PROVIDER
        )
        lastLocation?.let {
            mLocationListener?.onLocationChanged(it)
        }
        Log.d(RunnerLocationManager::class.java.name, "Location Added!")
    }

    fun removeLocationListener(){
        if (mlocationManager == null) return
        mLocationListener?.let { mlocationManager.removeUpdates(it) }
        mLocationListener = null
        Log.d(RunnerLocationManager::class.java.name, "Location Removed!")
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event ){
                Lifecycle.Event.ON_CREATE -> { Log.d(RunnerLocationManager::class.java.name, "onCreate()")}
                Lifecycle.Event.ON_START ->  { Log.d(RunnerLocationManager::class.java.name, "onStart()")}
                Lifecycle.Event.ON_RESUME -> { addLocationListener() }
                Lifecycle.Event.ON_PAUSE -> { removeLocationListener() }
                Lifecycle.Event.ON_STOP ->  { Log.d(RunnerLocationManager::class.java.name, "onStop()")}
                Lifecycle.Event.ON_DESTROY ->  { Log.d(RunnerLocationManager::class.java.name, "onDestroy()")}
                Lifecycle.Event.ON_ANY ->  { Log.d(RunnerLocationManager::class.java.name, "onAny()")}
        }
    }

}