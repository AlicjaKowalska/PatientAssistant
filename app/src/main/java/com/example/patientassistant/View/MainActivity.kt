package com.example.patientassistant.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.ViewModel.AppointmentViewModel
import com.example.patientassistant.ViewModel.AppointmentViewModelFactory
import com.example.patientassistant.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var appointmentViewModel: AppointmentViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appointmentViewModelFactory = AppointmentViewModelFactory((application as PatientApplication).appointmentRepository)

        appointmentViewModel = ViewModelProvider(this, appointmentViewModelFactory)[AppointmentViewModel::class.java]

//        val appointment = Appointment("AppointmentTitle", 10, 12, 2022, "Address")
//        appointmentViewModel.insert(appointment)
    }
}
