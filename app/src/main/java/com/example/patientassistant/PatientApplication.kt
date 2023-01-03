package com.example.patientassistant

import android.app.Application
import com.example.patientassistant.Repository.AppointmentRepository
import com.example.patientassistant.Repository.DrugRepository
import com.example.patientassistant.Room.PatientDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PatientApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { PatientDatabase.getDataBase(this, applicationScope) } //by lazy means that an instance of the database will be created only when the application is first needed, not every time it runs
    val drugRepository by lazy { DrugRepository(database.getDrugDao()) }
    val appointmentRepository by lazy { AppointmentRepository(this, database.getAppointmentDao()) }
}
