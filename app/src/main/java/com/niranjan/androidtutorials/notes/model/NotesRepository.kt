package com.niranjan.androidtutorials.notes.model

import com.niranjan.androidtutorials.notes.db.NotesDao
import com.niranjan.androidtutorials.notes.db.NotesEntity
import kotlinx.coroutines.flow.Flow

/***
 * Repository class provides a clean API for data access to the rest
 * of the APP.
 * Repository is not part of the Architecture Component Libraries, but
 * it is a suggested best practice for code separation and good architecture.
 * Repository manages queries and allows you to use multiple backends.
 * For Example:
 * 1. Fetch data from network
 * 2. Use data cached in a local database
 */
class NotesRepository(
    private val notesDao: NotesDao
) {

    val allNotes : Flow<List<NotesEntity>> = notesDao.getNotes()

    /**
     * suspend modifier tells the compiler that this needs to be called from a
     * coroutine or another suspending function
     */
    suspend fun insert(notesEntity: NotesEntity) {
        notesDao.insert(notesEntity)
    }
}