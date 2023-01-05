package com.example.patientassistant.Repository

import androidx.annotation.WorkerThread
import com.example.patientassistant.Model.Drug
import com.example.patientassistant.Room.DrugDAO
import kotlinx.coroutines.flow.Flow

class DrugRepository(private val drugDAO: DrugDAO) {

    val allDrugs: Flow<List<Drug>> = drugDAO.getAllDrugs()
    val todayDrugs: Flow<List<Drug>> = drugDAO.todayDrugs()

    @WorkerThread //annotations can be done in a single thread
    suspend fun insert(drug: Drug) {
        drugDAO.insert(drug)
    }

    @WorkerThread
    suspend fun update(drug: Drug) {
        drugDAO.update(drug)
    }

    @WorkerThread
    suspend fun delete(drug: Drug) {
        drugDAO.delete(drug)
    }

    @WorkerThread
    suspend fun deleteAllDrugs() {
        drugDAO.deleteAllDrugs()
    }
}
