package com.example.patientassistant.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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

    @Query ("DELETE FROM ${TableNames.drugTable}")
    suspend fun deleteAllDrugs()

    @Query("SELECT * FROM drug_table ORDER BY id ASC")
    fun getAllDrugs(): Flow<List<Drug>>

    @Query("SELECT * FROM drug_table ORDER BY hour, minute ASC")
    fun todayDrugs(): Flow<List<Drug>>

}
