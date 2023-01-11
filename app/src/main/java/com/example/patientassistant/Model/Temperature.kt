package com.example.patientassistant.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "temperature_table")
class Temperature(
    val temperature: Double,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int
) {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}