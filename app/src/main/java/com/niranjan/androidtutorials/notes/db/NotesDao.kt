package com.niranjan.androidtutorials.notes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


import kotlinx.coroutines.flow.Flow

/***
 * Flow is an async sequence of values
 * Flow produces values one at a time(instead of all at once) that can generate values
 * from async operations like network requests, database calls, or other async code.
 * Flow support coroutines, so you can transform flow using coroutines also.
 */
interface NotesInterface {
    fun getNotes(): Flow<List<NotesEntity>>
}

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes_table ORDER BY note ASC")
    fun getNotes(): Flow<List<NotesEntity>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notesEntity: NotesEntity)
    @Query("DELETE FROM notes_table")
    suspend fun deleteAll()
}