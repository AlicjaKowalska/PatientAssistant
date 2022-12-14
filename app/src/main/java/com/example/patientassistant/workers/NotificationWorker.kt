package com.example.patientassistant.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.patientassistant.Handlers.DrugNotificationHandler
import com.example.patientassistant.Objects.DrugNotificationConstants
import timber.log.Timber

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {

        val drugName = inputData.getString(DrugNotificationConstants.drugName)
        val drugDose = inputData.getString(DrugNotificationConstants.drugDose)
        val drugId = inputData.getString(DrugNotificationConstants.drugId)?.toInt()

        if (drugName != null && drugDose != null && drugId != null) {
            val drugNotificationHandler = DrugNotificationHandler(applicationContext)
            drugNotificationHandler.createReminderNotification(
                applicationContext, drugName, drugDose, drugId
            )
        } else {
            Timber.e(
                "One of the values is null - " +
                        "drugName: $drugName, drugDose: $drugDose, drugId: $drugId"
            )
        }
        return Result.success()
    }
}
