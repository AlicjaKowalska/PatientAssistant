package com.example.patientassistant.Handlers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.example.patientassistant.BroadcastReceivers.MedicalAppointmentReceiver
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.CommonAppointmentNotifications
import kotlinx.coroutines.flow.Flow
import java.util.*

class AppointmentNotificationsHandler(val context: Context) {

    fun createNotification(appointment: Appointment) {
        val currentTime = Calendar.getInstance().timeInMillis

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, appointment.year)
        calendar.set(Calendar.MONTH, appointment.month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, appointment.day)
        calendar.set(Calendar.HOUR_OF_DAY, appointment.hour)
        calendar.set(Calendar.MINUTE, appointment.minute)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MedicalAppointmentReceiver::class.java)
        intent.putExtra("appointmentId", appointment.id)
        intent.putExtra("appointmentTitle", "" + appointment.title)
        intent.putExtra("appointmentFacility", "" + appointment.facility)
        intent.putExtra("appointmentTime", "" + appointment.hour + ":" + appointment.minute)
        intent.putExtra(
            "appointmentDate",
            "" + appointment.day + "." + appointment.month + "." + appointment.year
        )

        val appointmentTimeHourBefore = calendar.timeInMillis - 3600000
        if(currentTime < appointmentTimeHourBefore) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                appointment.hashCode(),
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, appointmentTimeHourBefore, pendingIntent)
        }

        val appointmentTimeDayBefore = calendar.timeInMillis - (3600000 * 24)
        if(currentTime < appointmentTimeDayBefore) {
            val pendingIntent2 = PendingIntent.getBroadcast(
                context,
                appointment.hashCode() + 1, //added one to ensure that the two PendingIntent objects have distinct request codes (the hash code is a value that is computed based on the contents of the object)
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, appointmentTimeDayBefore, pendingIntent2)
        }
    }

    fun updateNotification(appointment: Appointment) {
        val notification = CommonAppointmentNotifications(
            context,
            appointment.title,
            "" + appointment.day + "." + appointment.month + "." + appointment.year,
            appointment.facility,
            "" + appointment.hour + ":" + appointment.minute)
        val updatedNotification = notification.buildNotification()

        NotificationManagerCompat.from(context).notify(appointment.id, updatedNotification.build())
    }

    fun deleteNotification(id: Int) {
        NotificationManagerCompat.from(context).cancel(id)
    }

    suspend fun deleteAllNotifications(myAllAppointments: Flow<List<Appointment>>) {
        myAllAppointments.collect { appointments ->
            appointments.forEach { appointment ->
                deleteNotification(appointment.id)
            }
        }
    }
}