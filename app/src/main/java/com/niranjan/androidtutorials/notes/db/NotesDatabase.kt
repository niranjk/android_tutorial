package com.niranjan.androidtutorials.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/***
 * Room Database
 * a database layer on top of SQLite database
 * it uses DAO to issue queries to its database
 * By default, Room doesn't allow to issue queries on main thread.
 * Room queries return Flow, the queries are automatically run asynchronously
 * on a background thread. This helps in nice UI performance.
 * Note * When you modify the database schema, you'll need to update the version number.
 */
@Database(entities = [NotesEntity::class], version = 1)
abstract class NotesDatabase : RoomDatabase(){
    abstract fun notesDao(): NotesDao

    companion object {
        private var INSTANCE: NotesDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): NotesDatabase{
            // if INSTANCE is not null, return it
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(NotesDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class NotesDatabaseCallback(
            private val scope: CoroutineScope
        ): Callback(){

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Clean and empty the data when app restarts
                INSTANCE?.let {
                    scope.launch(Dispatchers.IO){
                        populateDatabase(it.notesDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(notesDao: NotesDao){
            // Starts the app with a clean database each time.
            notesDao.deleteAll()
            val note = NotesEntity(note = "New Note!")
            notesDao.insert(note)
        }
    }
}