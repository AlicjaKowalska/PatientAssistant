package com.example.patientassistant.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.example.patientassistant.Fragment.DrugListFragment
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.R
import com.example.patientassistant.ViewModel.DrugViewModel
import com.example.patientassistant.ViewModel.DrugViewModelFactory
import com.example.patientassistant.databinding.ActivityDrugBinding

class DrugActivity : AppCompatActivity(R.layout.activity_drug) {

    lateinit var binding: ActivityDrugBinding
    lateinit var drugViewModel: DrugViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrugBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drugViewModelFactory =
            DrugViewModelFactory((application as PatientApplication).drugRepository)
        drugViewModel = ViewModelProvider(this, drugViewModelFactory)[DrugViewModel::class.java]

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace<DrugListFragment>(R.id.drugContainer)
            }
        }

        binding.bottomNavigation.selectedItemId = R.id.drugs
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