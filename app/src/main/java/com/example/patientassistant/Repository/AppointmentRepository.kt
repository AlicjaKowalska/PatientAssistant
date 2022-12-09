package com.example.patientassistant.Repository

import androidx.annotation.WorkerThread
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.Room.AppointmentDAO
import com.example.patientassistant.View.MedicalAppointment
import kotlinx.coroutines.flow.Flow

class AppointmentRepository (private val appointmentDAO: AppointmentDAO) {

    val myAllAppointments : Flow<List<Appointment>> = appointmentDAO.getAllAppointments()

    @WorkerThread //annotations can be done in a single thread
    suspend fun insert(appointment: Appointment) {
        appointmentDAO.insert(appointment)
    }
}
