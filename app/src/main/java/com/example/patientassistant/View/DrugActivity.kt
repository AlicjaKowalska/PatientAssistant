package com.example.patientassistant.View

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.patientassistant.Fragment.DrugListFragment
import com.example.patientassistant.Model.Drug
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.R
import com.example.patientassistant.ViewModel.DrugViewModel
import com.example.patientassistant.ViewModel.DrugViewModelFactory
import java.lang.Math.abs
import java.util.Calendar

class DrugActivity : AppCompatActivity(R.layout.activity_drug) {

    lateinit var drugViewModel: DrugViewModel
    lateinit var allDrugFromDatabase: List<Drug>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val drugViewModelFactory =
            DrugViewModelFactory((application as PatientApplication).drugRepository)
        drugViewModel = ViewModelProvider(this, drugViewModelFactory)[DrugViewModel::class.java]

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace<DrugListFragment>(R.id.drugContainer)
            }
        }

        //Alarm Manager

        val liveDataAllDrugsFromDatabase = drugViewModel.allDrugs
        allDrugFromDatabase = listOf()

        liveDataAllDrugsFromDatabase.observe(this, Observer {
                data ->
            allDrugFromDatabase = allDrugFromDatabase + data

            for (drug in allDrugFromDatabase) {
                checkHowMuchTimePassedAfterLastDose(drug)
            }

        })
    }

    private fun checkHowMuchTimePassedAfterLastDose(drug: Drug) {
        val timeLastDose = Calendar.getInstance()
        timeLastDose.set(Calendar.HOUR, drug.hour)
        timeLastDose.set(Calendar.MINUTE, drug.minute)

        val currentTime = Calendar.getInstance()
        val timePastLastDoseInSecond = (
                abs(currentTime.timeInMillis - timeLastDose.timeInMillis))/1000

        val timeNextDose = drug.interval * 3600
        createAlarm(timeNextDose)

//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Last dose")
//        builder.setMessage((timeLastDose.timeInMillis/1000).toString())
//        val dialog = builder.create()
//        dialog.show()


//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Title of the dialog")
//        builder.setMessage(timeDifferenceInSecond.toString())
//        val dialog = builder.create()
//        dialog.show()
    }

    private fun createAlarm(timeNextDose: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
//        calendar.add(Calendar.SECOND, timeNextDose)


        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, "notifyLemubit")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Take drug!")
            .setContentText("Take next dose of drug")
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(200, builder.build())

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis+1000, pendingIntent)

//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("next dose")
//        builder.setMessage((calendar.timeInMillis/1000).toString())
//        val dialog = builder.create()
//        dialog.show()
    }
}