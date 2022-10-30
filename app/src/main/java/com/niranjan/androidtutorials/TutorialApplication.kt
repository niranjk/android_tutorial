package com.niranjan.androidtutorials

import android.app.Application
import com.niranjan.androidtutorials.notes.db.NotesDatabase
import com.niranjan.androidtutorials.notes.model.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TutorialApplication : Application() {
    // this scope will be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    /***
     * Lazily create database and repository so that the instance is created
     * when needed rather than when application starts
     */
    val notesDatabase by lazy {
        NotesDatabase.getDatabase(this, applicationScope)
    }
    val notesRepository by lazy {
        NotesRepository(notesDatabase.notesDao())
    }
}