package com.example.patientassistant.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.patientassistant.R
import com.example.patientassistant.View.DrugActivity
import com.example.patientassistant.adapters.DrugAdapter
import com.example.patientassistant.databinding.FragmentDrugListBinding

class DrugListFragment : Fragment() {

    private lateinit var binding: FragmentDrugListBinding
    private lateinit var drugActivity: DrugActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrugListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drugActivity = activity as DrugActivity

        val drugAdapter = DrugAdapter()
        binding.recyclerViewDrugs.layoutManager = LinearLayoutManager(drugActivity)
        binding.recyclerViewDrugs.adapter = drugAdapter

        drugActivity.drugViewModel.allDrugs.observe(drugActivity) { drugs ->
            drugAdapter.setDrug(drugs)
        }


        setupAddDrugListener()
    }

    private fun setupAddDrugListener() {
        binding.floatingActionButtonAddDrug.setOnClickListener {
            drugActivity.supportFragmentManager.commit {
                replace<FragmentDrugForm>(R.id.drugContainer)
                addToBackStack("form")
            }
        }
    }
}
