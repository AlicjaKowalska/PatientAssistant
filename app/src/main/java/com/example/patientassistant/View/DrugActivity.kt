package com.example.patientassistant.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.example.patientassistant.Fragment.DrugListFragment
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.R
import com.example.patientassistant.ViewModel.DrugViewModel
import com.example.patientassistant.ViewModel.DrugViewModelFactory

class DrugActivity : AppCompatActivity(R.layout.activity_drug) {

    lateinit var drugViewModel: DrugViewModel

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
    }
}