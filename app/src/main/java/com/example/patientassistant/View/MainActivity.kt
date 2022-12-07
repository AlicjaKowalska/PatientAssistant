package com.example.patientassistant.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.R
import com.example.patientassistant.ViewModel.AppointmentViewModel
import com.example.patientassistant.ViewModel.AppointmentViewModelFactory
import com.example.patientassistant.ViewModel.DrugViewModel
import com.example.patientassistant.ViewModel.DrugViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var appointmentViewModel: AppointmentViewModel
    lateinit var drugViewModel: DrugViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drugViewModelFactory =
            DrugViewModelFactory((application as PatientApplication).drugRepository)
        val appointmentViewModelFactory =
            AppointmentViewModelFactory((application as PatientApplication).appointmentRepository)

        drugViewModel = ViewModelProvider(this, drugViewModelFactory)[DrugViewModel::class.java]
        appointmentViewModel =
            ViewModelProvider(this, appointmentViewModelFactory)[AppointmentViewModel::class.java]

//        val drug = Drug("DrugName", 1, 10, 10, 1)
//        drugViewModel.insert(drug)

        ////////////////////////////////////////////////////////////////////////////////////////////
        //testing purposes
        val appointment = Appointment("AppointmentTitle", 10, 12, 2022, "Address", 15, 30)

        for (i in 1..10)
            appointmentViewModel.insert(appointment)

        //starting MedicalAppointments activity - testing purposes
        val intent = Intent(this@MainActivity, MedicalAppointmentsActivity::class.java)
        startActivity(intent)
        ////////////////////////////////////////////////////////////////////////////////////////////
    }
}
