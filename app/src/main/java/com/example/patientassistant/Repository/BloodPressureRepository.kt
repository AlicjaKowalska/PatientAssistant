package com.example.patientassistant.Repository

import androidx.annotation.WorkerThread
import com.example.patientassistant.Model.BloodPressure
import com.example.patientassistant.Room.BloodPressureDAO
import kotlinx.coroutines.flow.Flow

class BloodPressureRepository(private val bloodPressureDAO: BloodPressureDAO) {

    val allBloodPressure: Flow<List<BloodPressure>> = bloodPressureDAO.getAllBloodPressure()

    @WorkerThread
    suspend fun insert(bloodPressure: BloodPressure) {
        bloodPressureDAO.insert(bloodPressure)
    }

    @WorkerThread
    suspend fun update(bloodPressure: BloodPressure) {
        bloodPressureDAO.update(bloodPressure)
    }

    @WorkerThread
    suspend fun delete(bloodPressure: BloodPressure) {
        bloodPressureDAO.delete(bloodPressure)
    }

    @WorkerThread
    suspend fun deleteAllBloodPressure() {
        bloodPressureDAO.deleteAllBloodPressure()
    }
}