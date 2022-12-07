package com.example.patientassistant.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.patientassistant.Adapters.AppointmentAdapter
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.R
import com.example.patientassistant.ViewModel.AppointmentViewModel
import com.example.patientassistant.ViewModel.AppointmentViewModelFactory

class MedicalAppointmentsActivity : AppCompatActivity() {

    lateinit var appointmentViewModel: AppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_appointments)

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
    }
}