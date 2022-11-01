package com.niranjan.androidtutorials.plants.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.niranjan.androidtutorials.plants.model.Plants

@Database(entities = [Plants::class], version = 1, exportSchema = false)
abstract class PlantsDatabase : RoomDatabase(){
    abstract fun plantDao(): PlantsDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: PlantsDatabase? = null

        fun getInstance(context: Context): PlantsDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PlantsDatabase {
            return Room.databaseBuilder(context, PlantsDatabase::class.java, DATABASE_NAME).build()
        }
    }
}
private  const val DATABASE_NAME = "plants_db"
