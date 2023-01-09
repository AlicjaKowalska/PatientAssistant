package com.example.patientassistant.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_table")
class Weight(
    val weight: Double,
    val year: Int,
    val month: Int,
    val day: Int
) {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}