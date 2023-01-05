package com.example.patientassistant.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.patientassistant.Model.Drug
import com.example.patientassistant.R
import com.example.patientassistant.databinding.DrugItemBinding

class DrugAdapter(private val clickListener: (Drug) -> Unit) : RecyclerView.Adapter<DrugAdapter.DrugViewHolder>() {

    lateinit var context: Context
    var drugs: List<Drug> = ArrayList()

    inner class DrugViewHolder(val binding: DrugItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugViewHolder {
        val binding = DrugItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return DrugViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrugViewHolder, position: Int) {
        val currentDrug: Drug = drugs[position]
        val drugName = currentDrug.name
        val drugSupportText = buildString {
            if (currentDrug.dose > 1) {
                append(context.getString(R.string.drug_details_dose_plural, currentDrug.dose))
            } else {
                append(context.getString(R.string.drug_details_dose_singular, currentDrug.dose))
            }
            append(" ")
            if (currentDrug.interval == 1) {
                append(context.getString(R.string.drug_details_interval_one_hour))
            } else {
                append(context.getString(R.string.drug_details_interval, currentDrug.interval))
            }
        }
        with(holder.binding) {
            tvDrugCapital.text = drugName.uppercase()[0].toString()
            tvDrugName.text = drugName
            tvDrugSupportText.text = drugSupportText
        }
        holder.itemView.setOnClickListener{
            clickListener(currentDrug)
        }
    }

    override fun getItemCount() = drugs.size

    fun setDrug(myDrugs: List<Drug>) {
        this.drugs = myDrugs
        notifyDataSetChanged()
    }
}