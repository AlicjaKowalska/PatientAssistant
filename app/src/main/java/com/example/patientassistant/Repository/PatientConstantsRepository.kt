package com.example.patientassistant.Repository

import androidx.annotation.WorkerThread
import com.example.patientassistant.Model.PatientConstants
import com.example.patientassistant.Room.PatientConstantsDAO

class PatientConstantsRepository(private val patientConstantsDAO: PatientConstantsDAO) {

    @WorkerThread
    suspend fun insert(patientConstants: PatientConstants) {
        patientConstantsDAO.insert(patientConstants)
    }

    @WorkerThread
    suspend fun update(patientConstants: PatientConstants) {
        patientConstantsDAO.update(patientConstants)
    }

    @WorkerThread
    suspend fun delete(patientConstants: PatientConstants) {
        patientConstantsDAO.delete(patientConstants)
    }
}