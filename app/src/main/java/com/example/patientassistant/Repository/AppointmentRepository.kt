package com.example.patientassistant.Repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.example.patientassistant.Handlers.AppointmentNotificationsHandler
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.Room.AppointmentDAO
import kotlinx.coroutines.flow.Flow
import java.util.*

class AppointmentRepository(
    private val context: Context,
    private val appointmentDAO: AppointmentDAO
) {

    val myAllAppointments: Flow<List<Appointment>> = appointmentDAO.getAllAppointments()
    val appointmentNotificationHandler = AppointmentNotificationsHandler(context)
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH) + 1 // Calendar month is 0-based
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    val todayAppointments: Flow<List<Appointment>> =
        appointmentDAO.todayAppointments(year, month, day)

    @WorkerThread //annotations can be done in a single thread
    suspend fun insert(appointment: Appointment) {
        appointmentDAO.insert(appointment)
        appointmentNotificationHandler.createNotification(appointment)
    }

    @WorkerThread
    suspend fun update(appointment: Appointment) {
        appointmentDAO.update(appointment)
        appointmentNotificationHandler.updateNotification(appointment)
    }

    @WorkerThread
    suspend fun delete(appointment: Appointment) {
        appointmentDAO.delete(appointment)
        appointmentNotificationHandler.deleteNotification(appointment.id)
    }

    @WorkerThread
    suspend fun deleteAllAppointments() {
        appointmentDAO.deleteAllAppointments()
        appointmentNotificationHandler.deleteAllNotifications(myAllAppointments)
    }
}
