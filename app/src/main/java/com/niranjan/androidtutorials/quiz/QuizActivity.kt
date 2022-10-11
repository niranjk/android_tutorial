package com.niranjan.androidtutorials.quiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.niranjan.androidtutorials.BuildConfig
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    lateinit var quizBinding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_quiz
        )
    }

    private fun navigateToExternalAppComponent(){
        val intent = Intent("com.my.external.app.ExternalActivity")
        if (intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    private fun sendEmail(){
        // EXPLICIT INTENT
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra( Intent.EXTRA_EMAIL, "myemailaddress@yamail.com")
            putExtra( Intent.EXTRA_TEXT, "Hello There.")
        }
        if (intent.resolveActivity(packageManager)!=null){
            startActivity(intent)
        }
    }
}