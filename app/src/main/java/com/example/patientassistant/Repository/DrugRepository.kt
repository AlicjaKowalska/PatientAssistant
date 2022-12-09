package com.example.patientassistant.Repository

import androidx.annotation.WorkerThread
import com.example.patientassistant.Model.Drug
import com.example.patientassistant.Room.DrugDAO

class DrugRepository (private val drugDAO: DrugDAO) {

    @WorkerThread //annotations can be done in a single thread
    suspend fun insert(drug : Drug) {
        drugDAO.insert(drug)
    }

    @WorkerThread
    suspend fun update(drug : Drug) {
        drugDAO.update(drug)
    }
}
