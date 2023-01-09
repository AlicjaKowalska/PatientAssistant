package com.example.patientassistant.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blood_pressure_table")
class BloodPressure(
    val systolic: Int,
    val diastolic: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int
) {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}