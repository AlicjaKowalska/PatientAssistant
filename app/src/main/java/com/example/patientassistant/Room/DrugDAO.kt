package com.example.patientassistant.Room

import androidx.room.Dao
import androidx.room.Insert
import com.example.patientassistant.Model.Drug

@Dao
interface DrugDAO {

    @Insert
    suspend fun insert(drug : Drug)

}
