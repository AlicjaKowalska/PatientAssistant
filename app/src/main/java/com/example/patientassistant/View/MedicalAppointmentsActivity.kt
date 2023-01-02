package com.example.patientassistant.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.patientassistant.Adapters.AppointmentAdapter
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.R
import com.example.patientassistant.ViewModel.AppointmentViewModel
import com.example.patientassistant.ViewModel.AppointmentViewModelFactory
import com.example.patientassistant.databinding.ActivityMedicalAppointmentsBinding

class MedicalAppointmentsActivity : AppCompatActivity() {

    lateinit var binding: ActivityMedicalAppointmentsBinding
    lateinit var appointmentViewModel: AppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicalAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = findViewById(R.id.medicalAppointmentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val appointmentAdapter = AppointmentAdapter()
        recyclerView.adapter = appointmentAdapter

        val appointmentViewModelFactory =
            AppointmentViewModelFactory((application as PatientApplication).appointmentRepository)

        appointmentViewModel =
            ViewModelProvider(this, appointmentViewModelFactory)[AppointmentViewModel::class.java]

        appointmentViewModel.myAllAppointments.observe(this) { appointment ->
            //update UI
            appointmentAdapter.setAppointments(appointment)
        }

        binding.bottomNavigation.selectedItemId = R.id.appointments
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    Log.i("Menu", "Home selected")
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
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
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }
}