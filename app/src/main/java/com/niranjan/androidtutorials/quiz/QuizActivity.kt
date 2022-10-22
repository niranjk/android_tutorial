package com.niranjan.androidtutorials.quiz

import android.content.Intent
import android.os.Bundle
import com.niranjan.androidtutorials.drawer.DrawerBaseActivity
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.ActivityQuizBinding

class QuizActivity : DrawerBaseActivity() {

    lateinit var quizBinding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(quizBinding.root)
        allocateActivityTitle(getString(R.string.label_quiz_app))
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