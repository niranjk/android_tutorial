package com.niranjan.androidtutorials.notes.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/***
 * Room creates tables via a Entity.
 * Entity : is an Annotated class that describes a database table
 * when working with Room.
 */
@Entity(tableName = "notes_table")
data class NotesEntity(
    /**
     * Each property in the class will represent a
     * column in the table.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "note")
    val note: String
)
