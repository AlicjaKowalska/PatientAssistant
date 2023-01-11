package com.example.patientassistant.Repository

import androidx.annotation.WorkerThread
import com.example.patientassistant.Model.Weight
import com.example.patientassistant.Room.WeightDAO
import kotlinx.coroutines.flow.Flow

class WeightRepository(private val weightDAO: WeightDAO) {

    val allWeight: Flow<List<Weight>> = weightDAO.getAllWeight()

    @WorkerThread
    suspend fun insert(weight: Weight) {
        weightDAO.insert(weight)
    }

    @WorkerThread
    suspend fun update(weight: Weight) {
        weightDAO.update(weight)
    }

    @WorkerThread
    suspend fun delete(weight: Weight) {
        weightDAO.delete(weight)
    }

    @WorkerThread
    suspend fun deleteAllWeight() {
        weightDAO.deleteAllWeight()
    }
}