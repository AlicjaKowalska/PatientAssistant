package com.example.patientassistant.Room

import androidx.room.*
import com.example.patientassistant.Model.Drug
import com.example.patientassistant.Objects.TableNames
import kotlinx.coroutines.flow.Flow

@Dao
interface DrugDAO {

    @Insert
    suspend fun insert(drug: Drug)

    @Update
    suspend fun update(drug: Drug)

    @Delete
    suspend fun delete(drug: Drug)

    @Query("DELETE FROM ${TableNames.drugTable}")
    suspend fun deleteAllDrugs()

    @Query("SELECT * FROM ${TableNames.drugTable} ORDER BY id ASC")
    fun getAllDrugs(): Flow<List<Drug>>

    @Query("SELECT * FROM ${TableNames.drugTable} ORDER BY hour, minute ASC")
    fun todayDrugs(): Flow<List<Drug>>

}
