package com.example.patientassistant.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import com.example.patientassistant.Model.Drug
import kotlinx.coroutines.flow.Flow

@Dao
interface DrugDAO {

    @Insert
    suspend fun insert(drug : Drug)

    @Update
    suspend fun update(drug : Drug)

    @Query("SELECT * FROM drug_table ORDER BY id ASC")
    fun getAllDrugs(): Flow<List<Drug>>

}
