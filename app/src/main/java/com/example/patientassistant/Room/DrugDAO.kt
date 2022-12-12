package com.example.patientassistant.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.example.patientassistant.Model.Drug

@Dao
interface DrugDAO {

    @Insert
    suspend fun insert(drug : Drug)

    @Update
    suspend fun update(drug : Drug)

}
