package com.niranjan.androidtutorials.plants.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.niranjan.androidtutorials.plants.model.Plants
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantsDao {

    /**
     * Using LiveData as return type
     */
    @Query("SELECT * FROM plants_db ORDER BY name")
    fun getPlants(): LiveData<List<Plants>>
    /**
     * Using Flow in Room db return type.
     */
    @Query("SELECT * FROM plants_db ORDER BY name")
    fun getPlantsFlow(): Flow<List<Plants>>

    @Query("SELECT * FROM plants_db WHERE growZoneNumber = :growZoneNumber ORDER BY name")
    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): LiveData<List<Plants>>

    @Query("SELECT * FROM plants_db WHERE growZoneNumber = :growZoneNumber ORDER BY name")
    fun getPlantsWithGrowZoneNumberFlow(growZoneNumber: Int): Flow<List<Plants>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<Plants>)
}