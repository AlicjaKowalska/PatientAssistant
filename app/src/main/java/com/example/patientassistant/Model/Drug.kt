package com.example.patientassistant.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drug_table")
class Drug (var name : String, var dose : Int, var hour : Int, var minute : Int, var interval : Int) {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}
