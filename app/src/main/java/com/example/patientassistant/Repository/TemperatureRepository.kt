package com.example.patientassistant.Repository

import androidx.annotation.WorkerThread
import com.example.patientassistant.Model.Temperature
import com.example.patientassistant.Room.TemperatureDAO
import kotlinx.coroutines.flow.Flow

class TemperatureRepository(private val temperatureDAO: TemperatureDAO) {

    val allTemperature: Flow<List<Temperature>> = temperatureDAO.getAllTemperature()

    @WorkerThread
    suspend fun insert(temperature: Temperature) {
        temperatureDAO.insert(temperature)
    }

    @WorkerThread
    suspend fun update(temperature: Temperature) {
        temperatureDAO.update(temperature)
    }

    @WorkerThread
    suspend fun delete(temperature: Temperature) {
        temperatureDAO.delete(temperature)
    }

    @WorkerThread
    suspend fun deleteAllTemperature() {
        temperatureDAO.deleteAllTemperature()
    }
}