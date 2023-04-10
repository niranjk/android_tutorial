package com.niranjan.androidtutorials.people

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.niranjan.androidtutorials.R

class PeopleActivity : AppCompatActivity(R.layout.activity_people) {

    companion object {
        private const val FRAGMENT_CHAT = "chat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}