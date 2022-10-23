package com.niranjan.androidtutorials.runner

import androidx.lifecycle.ViewModel

class StopWatchViewModel : ViewModel() {

    private var mStartTime: Long? = null

    fun getStartTime(): Long? = mStartTime
    fun setStartTime(value: Long){
        mStartTime = value
    }

}