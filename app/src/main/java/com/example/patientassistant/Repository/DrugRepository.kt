package com.example.patientassistant.Repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.example.patientassistant.Handlers.DrugNotificationHandler
import com.example.patientassistant.Model.Drug
import com.example.patientassistant.Room.DrugDAO
import kotlinx.coroutines.flow.Flow

class DrugRepository(private val drugDAO: DrugDAO, val context: Context) {

    val allDrugs: Flow<List<Drug>> = drugDAO.getAllDrugs()
    val drugNotificationHandler = DrugNotificationHandler(context)
    val todayDrugs: Flow<List<Drug>> = drugDAO.todayDrugs()

    @WorkerThread //annotations can be done in a single thread
    suspend fun insert(drug: Drug) {
        drugDAO.insert(drug)
        drugNotificationHandler.startNotificationTakingNextDose(drug)
    }

    @WorkerThread
    suspend fun update(drug: Drug) {
        drugDAO.update(drug)
        drugNotificationHandler.editNotificationTakingNextDose(drug)
    }

    @WorkerThread
    suspend fun delete(drug: Drug) {
        drugDAO.delete(drug)
        drugNotificationHandler.stopNotificationTakingNextDose(drug)
    }

    @WorkerThread
    suspend fun deleteAllDrugs() {
        drugNotificationHandler.stopNotificationTakingNextDoseAllDrugs(allDrugs)
        drugDAO.deleteAllDrugs()
    }
}
