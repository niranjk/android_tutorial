package com.niranjan.androidtutorials.plants.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.niranjan.androidtutorials.plants.model.Plants

@Dao
interface PlantsDao {
    @Query("SELECT * FROM plants_db ORDER BY name")
    fun getPlants(): LiveData<List<Plants>>

    @Query("SELECT * FROM plants_db WHERE growZoneNumber = :growZoneNumber ORDER BY name")
    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): LiveData<List<Plants>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<Plants>)
}