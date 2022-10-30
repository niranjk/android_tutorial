package com.niranjan.androidtutorials.notes.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.TutorialApplication
import com.niranjan.androidtutorials.databinding.ActivityNotesBinding
import com.niranjan.androidtutorials.drawer.DrawerBaseActivity
import com.niranjan.androidtutorials.notes.db.NotesEntity
import com.niranjan.androidtutorials.notes.viewmodel.NotesViewModel
import com.niranjan.androidtutorials.notes.viewmodel.NotesViewModelFactory

class NotesActivity : DrawerBaseActivity() {

    lateinit var notesBinding: ActivityNotesBinding
    private var notesAdapter = NotesAdapter()

    /***
     * To create the ViewModel we use the viewModels delegate,
     * passing in instance of NotesViewModelFactory.
     */
    private val notesViewModel: NotesViewModel by viewModels {
        NotesViewModelFactory(
            (application as TutorialApplication).notesRepository
        )
    }

    private val startNewNotesActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK){
                result.data?.getStringExtra(NewNotesActivity.EXTRA_REPLY)?.let {
                    val note = NotesEntity(note = it)
                    notesViewModel.insert(note)
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesBinding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(notesBinding.root)
        allocateActivityTitle(getString(R.string.label_notes_app))
        setView()
        setListeners()
        setObservers()
    }

    private fun setView(){
        with(notesBinding){
            recyclerview.adapter = notesAdapter
            recyclerview.layoutManager = LinearLayoutManager(this@NotesActivity)
        }
    }

    private fun setListeners(){
        notesBinding.fab.setOnClickListener {
            startNewNotesActivityForResult.launch(
                Intent(
                    this@NotesActivity,
                    NewNotesActivity::class.java
                )
            )
        }
    }

    /***
     * Observe the LiveData and update view
     */
    private fun setObservers(){
        notesViewModel.allNotes.observe(this){ notesList ->
            notesList?.let {
                notesAdapter.submitList(it)
            }
        }
    }
}