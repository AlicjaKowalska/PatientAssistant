package com.example.patientassistant.Room

import androidx.room.*
import com.example.patientassistant.Model.BloodPressure
import com.example.patientassistant.Objects.TableNames
import kotlinx.coroutines.flow.Flow

@Dao
interface BloodPressureDAO {

    @Insert
    suspend fun insert(bloodPressure: BloodPressure)

    @Update
    suspend fun update(bloodPressure: BloodPressure)

    @Delete
    suspend fun delete(bloodPressure: BloodPressure)

    @Query("SELECT * FROM ${TableNames.bloodPressureTable}")
    fun getAllBloodPressure(): Flow<List<BloodPressure>>

    @Query("DELETE FROM ${TableNames.bloodPressureTable}")
    suspend fun deleteAllBloodPressure()
}