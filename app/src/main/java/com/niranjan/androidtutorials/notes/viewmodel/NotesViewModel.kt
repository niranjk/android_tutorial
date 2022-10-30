package com.niranjan.androidtutorials.notes.viewmodel

import androidx.lifecycle.*
import com.niranjan.androidtutorials.notes.db.NotesEntity
import com.niranjan.androidtutorials.notes.model.NotesRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

/***
 * ViewModel
 * provide data to the UI and survice configuration changes.
 * View - ViewModel - Repository
 * We can also use ViewModel to share data between Fragments.
 * ViewModel holds and process all the data needed for the UI.
 */

/***
 * @Warnings
 * 1. Don't keep a reference to a @Context in ViewModel
 * 2. ViewModel don't survive the app's process being killed in the background
 * when the OS needs more resources. For UI that needs to survive process death
 * due to running out of resources use : Saved State Module for ViewModel
 */
class NotesViewModel(
    private val repository: NotesRepository
): ViewModel() {

    /***
     * LiveData is an observable data holder - you get notified every time the data changes.
     * LiveData is lifecycle aware, it will stop or resume observation depending on the
     * lifecycle of the component that listens for changes.
     * We can put an observer on the data (instead of polling for changes) and only
     * update the UI when the data actually changes.
     */

    /**
     * here the ViewModel transform the data from Repository, from Flow to LiveData and
     * exposes the list of notes as LiveData to the UI.
     */
    val allNotes: LiveData<List<NotesEntity>> = repository.allNotes.asLiveData()

    /***
     * Launch a new coroutine to insert the data in a non-blocking way
     * In Kotlin, all coroutines run inside a CoroutineScope.
     * A scope controls the lifetime of coroutines through its job.
     * When you cancel the job of a scope, it cancels all coroutines started in that scope.
     * The AndroidX lifecycle-viewmodel-ktx library adds a viewModelScope as an extension
     * funciton of the ViewModel class, enabling to work with scopes.
     */
    fun insert(notes: NotesEntity) = viewModelScope.launch {
        repository.insert(notes)
    }
}

/***
 * ViewModelProvider.Factory gets as a paramter the dependencies needed to create
 * NotesViewModel
 */
class NotesViewModelFactory(
    private val repository: NotesRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)){
            return NotesViewModel(repository) as T
        }
        throw IllegalArgumentException ("Unknown ViewModel class")
    }
}