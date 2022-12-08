package com.example.patientassistant.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
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

        val drugViewModelFactory = DrugViewModelFactory((application as PatientApplication).drugRepository)
        val appointmentViewModelFactory = AppointmentViewModelFactory((application as PatientApplication).appointmentRepository)

        drugViewModel = ViewModelProvider(this, drugViewModelFactory)[DrugViewModel::class.java]
        appointmentViewModel = ViewModelProvider(this, appointmentViewModelFactory)[AppointmentViewModel::class.java]


        val intent = Intent(this@MainActivity, MedicalAppointment::class.java)
        startActivity(intent)

//        val drug = Drug("DrugName", 1, 10, 10, 1)
//        val appointment = Appointment("AppointmentTitle", 10, 12, 2022, "Address")
//
//        drugViewModel.insert(drug)
//        appointmentViewModel.insert(appointment)
    }
}
