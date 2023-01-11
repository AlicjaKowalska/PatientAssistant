package com.example.patientassistant.Room

import androidx.room.*
import com.example.patientassistant.Model.Temperature
import com.example.patientassistant.Objects.TableNames
import kotlinx.coroutines.flow.Flow

@Dao
interface TemperatureDAO {

    @Insert
    suspend fun insert(temperature: Temperature)

    @Update
    suspend fun update(temperature: Temperature)

    @Delete
    suspend fun delete(temperature: Temperature)

    @Query("SELECT * FROM ${TableNames.temperatureTable}")
    fun getAllTemperature(): Flow<List<Temperature>>

    @Query("DELETE FROM ${TableNames.temperatureTable}")
    suspend fun deleteAllTemperature()
}