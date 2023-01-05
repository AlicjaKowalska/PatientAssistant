package com.example.patientassistant

import android.content.Context
import androidx.core.app.NotificationCompat

class CommonAppointmentNotifications(
    val context: Context,
    val appointmentTitle: String,
    val appointmentDate: String,
    val appointmentFacility: String, val appointmentTime: String
) {

    val channelId = "appointment_reminder"

    fun buildNotification(): NotificationCompat.Builder {
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText("" + appointmentTime + " " + appointmentDate + "\n" + appointmentFacility)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(appointmentTitle)
            .setStyle(bigTextStyle)

        return notification
    }
}