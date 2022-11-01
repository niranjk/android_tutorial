package com.niranjan.androidtutorials.plants.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants_db")
data class Plants(
    @PrimaryKey @ColumnInfo(name = "id") val plantId: String,
    val name: String,
    val description: String,
    val growZoneNumber: Int,
    val wateringInterval: Int = 7, // how often the plant should be watered, in days
    val imageUrl: String = ""
){
    override fun toString() = name
}


