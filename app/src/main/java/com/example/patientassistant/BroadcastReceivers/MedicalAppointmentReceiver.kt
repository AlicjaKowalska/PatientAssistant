package com.example.patientassistant.BroadcastReceivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.patientassistant.CommonAppointmentNotifications
import com.example.patientassistant.R
import com.example.patientassistant.View.MainActivity

class MedicalAppointmentReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras
        val appointmentId = extras?.getInt("appointmentId")
        val appointmentTitle = extras?.getString("appointmentTitle")
        val appointmentFacility = extras?.getString("appointmentFacility")
        val appointmentTime = extras?.getString("appointmentTime")
        val appointmentDate = extras?.getString("appointmentDate")

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "appointment_reminder"

        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel(channelId, context.resources.getString(R.string.appointment_reminders), importance)
        notificationManager.createNotificationChannel(channel)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationCommon = CommonAppointmentNotifications(
            context,
            appointmentTitle!!,
            appointmentDate!!,
            appointmentFacility!!,
            appointmentTime!!
        )
        val notification = notificationCommon.buildNotification()
            .setContentText(context.resources.getString(R.string.appointment_coming_up))
            .setSmallIcon(R.drawable.appointment)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (appointmentId != null) {
            notificationManager.notify(appointmentId, notification)
        }
    }
}
