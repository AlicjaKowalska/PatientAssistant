package com.example.patientassistant.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.R
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

        val appointmentViewModelFactory =
            AppointmentViewModelFactory((application as PatientApplication).appointmentRepository)

        appointmentViewModel =
            ViewModelProvider(this, appointmentViewModelFactory)[AppointmentViewModel::class.java]

        binding.bottomNavigation.selectedItemId = R.id.home
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    Log.i("Menu", "Home selected")
                    return@setOnItemSelectedListener true
                }
                R.id.drugs -> {
                    Log.i("Menu", "Drugs selected")
                    startActivity(Intent(this, DrugActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.appointments -> {
                    Log.i("Menu", "Appointments selected")
                    startActivity(Intent(this, MedicalAppointmentsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }
}
