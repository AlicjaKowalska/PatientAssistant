package com.example.patientassistant.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_constants_table")
class PatientConstants(
    val name: String,
    val height: Double,
    val yearOfBirth: Int,
    val monthOfBirth: Int,
    val dayOfBirth: Int
) {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}