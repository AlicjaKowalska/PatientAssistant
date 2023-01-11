package com.example.patientassistant.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.patientassistant.Adapters.AppointmentAdapter
import com.example.patientassistant.Adapters.DrugAdapter
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.R
import com.example.patientassistant.ViewModel.AppointmentViewModel
import com.example.patientassistant.ViewModel.AppointmentViewModelFactory
import com.example.patientassistant.ViewModel.DrugViewModel
import com.example.patientassistant.ViewModel.DrugViewModelFactory
import com.example.patientassistant.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var appointmentViewModel: AppointmentViewModel
    lateinit var drugsViewModel: DrugViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerViewDrugs: RecyclerView = findViewById(R.id.drugsRecyclerView)
        recyclerViewDrugs.layoutManager = LinearLayoutManager(this)
        val drugsAdapter = DrugAdapter { drug -> }
        recyclerViewDrugs.adapter = drugsAdapter

        initDrugsView(drugsAdapter)
        initAppointmentsView(drugsAdapter, recyclerViewDrugs)

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

    private fun initDrugsView(drugsAdapter: DrugAdapter) {
        val drugsModelFactory =
            DrugViewModelFactory((application as PatientApplication).drugRepository)

        drugsViewModel =
            ViewModelProvider(this, drugsModelFactory)[DrugViewModel::class.java]

        drugsViewModel.todayDrugs.observe(this) { drug ->
            drugsAdapter.setDrug(drug)
            if (drugsAdapter.drugs.isEmpty())
                binding.textViewDrugs.isInvisible = true
        }
    }

    private fun initAppointmentsView(drugsAdapter: DrugAdapter, recyclerViewDrugs: RecyclerView) {
        val recyclerViewAppointments: RecyclerView =
            findViewById(R.id.medicalAppointmentRecyclerView)
        recyclerViewAppointments.layoutManager = LinearLayoutManager(this)
        val appointmentAdapter = AppointmentAdapter()
        recyclerViewAppointments.adapter = appointmentAdapter

        val appointmentViewModelFactory =
            AppointmentViewModelFactory((application as PatientApplication).appointmentRepository)

        appointmentViewModel =
            ViewModelProvider(this, appointmentViewModelFactory)[AppointmentViewModel::class.java]

        appointmentViewModel.todayAppointments.observe(this) { appointment ->
            //update UI
            appointmentAdapter.setAppointments(appointment)
            if (appointmentAdapter.appointments.isEmpty())
                binding.textViewAppointments.isInvisible = true
            if (appointmentAdapter.appointments.isEmpty() && drugsAdapter.drugs.isNotEmpty()) {
                val layoutParams = recyclerViewDrugs.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.matchConstraintPercentHeight = 1.0f
                recyclerViewDrugs.layoutParams = layoutParams
            }
            if (drugsAdapter.drugs.isEmpty() && appointmentAdapter.appointments.isNotEmpty()) {

                val parentViewGroup = binding.textViewAppointments.parent as ViewGroup
                parentViewGroup.removeView(binding.textViewAppointments)
                parentViewGroup.removeView(binding.medicalAppointmentRecyclerView)

                val constraintLayout = ConstraintLayout(applicationContext)
                val paramsTextViewAppointments = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                val density = resources.displayMetrics.density

                paramsTextViewAppointments.topMargin = (30 * density).toInt()
                paramsTextViewAppointments.marginStart = (16 * density).toInt()
                paramsTextViewAppointments.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                paramsTextViewAppointments.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                binding.textViewAppointments.layoutParams = paramsTextViewAppointments
                binding.textViewAppointments.gravity = Gravity.TOP
                constraintLayout.addView(binding.textViewAppointments)

                recyclerViewAppointments.layoutParams.height = 0
                constraintLayout.addView(recyclerViewAppointments)

                parentViewGroup.addView(constraintLayout)
            }
        }
    }
}
