package com.example.patientassistant

import android.app.Application
import com.example.patientassistant.Repository.*
import com.example.patientassistant.Room.PatientDatabase
import com.example.patientassistant.Room.PatientProfileDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PatientApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        PatientDatabase.getDataBase(
            this,
            applicationScope
        )
    } //by lazy means that an instance of the database will be created only when the application is first needed, not every time it runs
    val drugRepository by lazy { DrugRepository(database.getDrugDao(), this) }
    val appointmentRepository by lazy { AppointmentRepository(this, database.getAppointmentDao()) }

    val patientProfileDatabase by lazy { PatientProfileDatabase.getDataBase(this, applicationScope) }
    val bloodPressureRepository by lazy { BloodPressureRepository(patientProfileDatabase.getBloodPressureDao())}
    val patientConstantsRepository by lazy { PatientConstantsRepository(patientProfileDatabase.getPatientConstantsDao())}
    val temperatureRepository by lazy {TemperatureRepository(patientProfileDatabase.getTemperatureDao())}
    val weightRepository by lazy {WeightRepository(patientProfileDatabase.getWeightDao())}
}
