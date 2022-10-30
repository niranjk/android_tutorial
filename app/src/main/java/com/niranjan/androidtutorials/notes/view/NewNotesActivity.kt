package com.niranjan.androidtutorials.notes.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.ActivityNewNotesBinding
import com.niranjan.androidtutorials.drawer.DrawerBaseActivity

class NewNotesActivity : DrawerBaseActivity() {

    lateinit var newNotesBinding: ActivityNewNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newNotesBinding = ActivityNewNotesBinding.inflate(layoutInflater)
        setContentView(newNotesBinding.root)
        allocateActivityTitle(getString(R.string.label_new_notes))
        setListeners()
    }

    private fun setListeners(){
        newNotesBinding.buttonSave.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(newNotesBinding.editWord.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val notes = newNotesBinding.editWord.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, notes)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "REPLY"
    }
}