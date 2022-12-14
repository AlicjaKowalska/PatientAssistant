package com.example.patientassistant.Handlers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.patientassistant.Model.Drug
import com.example.patientassistant.Objects.DrugNotificationConstants
import com.example.patientassistant.R
import com.example.patientassistant.View.DrugActivity
import com.example.patientassistant.workers.NotificationWorker
import kotlinx.coroutines.flow.Flow
import java.util.*
import java.util.concurrent.TimeUnit

class DrugNotificationHandler(val context: Context) {
    private val CHANNEL_ID = "drug_take_notification"

    fun startNotificationTakingNextDose(drug: Drug) {

        val timeLastDose = Calendar.getInstance()
        timeLastDose.set(Calendar.HOUR, drug.hour)
        timeLastDose.set(Calendar.MINUTE, drug.minute)

        val drugInterval = drug.interval.toLong()
        val timeNextDose = timeLastDose.timeInMillis + drugInterval * 3600000
        val timeToNextDose = timeNextDose - System.currentTimeMillis()

        val data = Data.Builder()
            .putString(DrugNotificationConstants.drugName, drug.name)
            .putString(DrugNotificationConstants.drugDose, drug.dose.toString())
            .putString(DrugNotificationConstants.drugId, drug.id.toString())
            .build()

        val notificationRequest = PeriodicWorkRequest.Builder(
            NotificationWorker::class.java,
            drugInterval,
            TimeUnit.HOURS
        )
            .setInputData(data)
            .setInitialDelay(timeToNextDose, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "drug_${drug.name}_take_notification",
                ExistingPeriodicWorkPolicy.KEEP,
                notificationRequest
            )
    }

    fun stopNotificationTakingNextDose(drug: Drug) {
        WorkManager.getInstance(context).cancelUniqueWork(
            "drug_${drug.name}_take_notification"
        )
    }

    fun editNotificationTakingNextDose(drug: Drug) {

        val timeLastDose = Calendar.getInstance()
        timeLastDose.set(Calendar.HOUR, drug.hour)
        timeLastDose.set(Calendar.MINUTE, drug.minute)

        val drugInterval = drug.interval.toLong()
        val timeNextDose = timeLastDose.timeInMillis + drugInterval * 3600000
        val timeToNextDose = timeNextDose - System.currentTimeMillis()

        val notificationRequest = PeriodicWorkRequest.Builder(
            NotificationWorker::class.java,
            drugInterval,
            TimeUnit.HOURS
        )
            .setInitialDelay(timeToNextDose, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "drug_${drug.name}_take_notification",
                ExistingPeriodicWorkPolicy.REPLACE,
                notificationRequest
            )
    }

    suspend fun stopNotificationTakingNextDoseAllDrugs(allDrugs: Flow<List<Drug>>) {
        allDrugs.collect { drugList ->
            drugList.forEach { drug ->
                stopNotificationTakingNextDose(drug)
            }
        }
    }

    fun createReminderNotification(
        context: Context,
        drugName: String,
        drugDose: String,
        drugId: Int
    ) {
        val intent = Intent(context, DrugActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, drugId, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )

        createNotificationChannel(context) // This won't create a new channel everytime, safe to call

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.drug)
            .setContentTitle(
                String.format(
                    context.getString(
                        R.string.drug_taking_notification_title
                    ), drugName
                )
            )
            .setContentText(
                String.format(
                    context.getString(
                        R.string.drug_taking_notification_content_text
                    ), drugDose
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // For launching the MainActivity
            .setAutoCancel(true) // Remove notification when tapped
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Show on lock screen

        with(NotificationManagerCompat.from(context)) {
            notify(drugId, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_description_text)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}