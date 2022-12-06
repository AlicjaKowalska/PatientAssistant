package com.example.patientassistant.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.patientassistant.Model.Drug
import com.example.patientassistant.databinding.DrugItemBinding

class DrugAdapter : RecyclerView.Adapter<DrugAdapter.DrugViewHolder>() {

    var drugs: List<Drug> = ArrayList()

    inner class DrugViewHolder(val binding: DrugItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugViewHolder {
        val binding = DrugItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DrugViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrugViewHolder, position: Int) {
        val currentDrug: Drug = drugs[position]
        val drugName = currentDrug.name
        val drugSupportText = buildString {
            append(currentDrug.dose)
            append(" ")
            if (currentDrug.dose > 1) append("tablets ") else append("tablet ")
            append("every ")
            if (currentDrug.interval > 1) {
                append(currentDrug.interval)
                append(" hours")
            } else {
                append("hour")
            }
        }
        with(holder) {
            with(binding) {
                tvDrugCapital.text = drugName.uppercase()[0].toString()
                tvDrugName.text = drugName
                tvDrugSupportText.text = drugSupportText
            }
        }
    }

    override fun getItemCount(): Int {
        return drugs.size
    }

    fun setDrug(myDrugs: List<Drug>) {
        this.drugs = myDrugs
        notifyDataSetChanged()
    }
}