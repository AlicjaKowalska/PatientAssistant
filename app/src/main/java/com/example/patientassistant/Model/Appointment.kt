package com.example.patientassistant.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointment_table")
class Appointment (val title : String, val day : Int, val month : Int, val year : Int, val facility : String) {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}
