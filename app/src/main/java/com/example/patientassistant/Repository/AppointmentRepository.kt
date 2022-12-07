package com.example.patientassistant.Repository

import androidx.annotation.WorkerThread
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.Room.AppointmentDAO

class AppointmentRepository (private val appointmentDAO: AppointmentDAO) {

    @WorkerThread //annotations can be done in a single thread
    suspend fun insert(appointment : Appointment) {
        appointmentDAO.insert(appointment)
    }
}
