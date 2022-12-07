package com.example.patientassistant.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drug_table")
class Drug (val name : String, val dose : Int, val hour : Int, val minute : Int, val interval : Int) {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}
