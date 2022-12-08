package com.example.patientassistant.Fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.patientassistant.Model.Drug
import com.example.patientassistant.R
import com.example.patientassistant.View.DrugActivity
import com.example.patientassistant.databinding.FragmentDrugFormBinding
import java.text.SimpleDateFormat
import java.util.*

class FragmentDrugForm : Fragment(), TimePickerDialog.OnTimeSetListener {

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            when (view.id) {
                R.id.drugName -> {
                    validateDrugName()
                }
                R.id.dose -> {
                    validateDose()
                }
                R.id.doseInterval -> {
                    validateDoseInterval()
                }
                R.id.doseTime -> {
                    validateTime()
                }
            }
        }

    }

    private lateinit var binding: FragmentDrugFormBinding
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("hh:mm", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrugFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.drugName.addTextChangedListener(TextFieldValidation(binding.drugName))
        binding.dose.addTextChangedListener(TextFieldValidation(binding.dose))
        binding.doseInterval.addTextChangedListener(TextFieldValidation(binding.doseInterval))
        binding.doseTime.addTextChangedListener(TextFieldValidation(binding.doseTime))

        binding.button.setOnClickListener {
            if (!validateDrugName() || !validateDose() || !validateDoseInterval() || !validateTime()) {
                return@setOnClickListener
            }
            saveDrug()
            //close fragment and go back to list of drugs
        }

        binding.doseTime.setOnClickListener {
            TimePickerDialog(
                /* context = */ requireActivity(),
                /* listener = */ this,
                /* hourOfDay = */ calendar.get(Calendar.HOUR_OF_DAY),
                /* minute = */ calendar.get(Calendar.MINUTE),
                /* is24HourView = */ true
            ).show()
        }
    }

    private fun saveDrug() {
        val drugName: String = binding.drugName.text.toString()
        val dose: Int = binding.dose.text.toString().toInt()
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)
        val interval: Int = binding.doseInterval.text.toString().toInt()

        val drug = Drug(drugName, dose, hour, minute, interval)
        val drugActivity = activity as DrugActivity
        drugActivity.drugViewModel.insert(drug)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        displayFormattedDate(calendar.timeInMillis)
    }

    private fun displayFormattedDate(timestamp: Long) {
        binding.doseTime.setText(formatter.format(timestamp))
    }

    private fun validateDrugName() = if (binding.drugName.text.toString().trim().isEmpty()) {
        binding.drugNameTextInputLayout.error = getString(R.string.error_required)
        false
    } else {
        binding.drugNameTextInputLayout.isErrorEnabled = false
        true
    }

    private fun validateDose() = if (binding.dose.text.toString().isEmpty()) {
        binding.doseTextInputLayout.error = getString(R.string.error_required)
        false
    } else if (binding.dose.text.toString().toInt() <= 0) {
        binding.doseTextInputLayout.error = getString(R.string.error_dose_lesser_equal_0)
        false
    } else {
        binding.doseTextInputLayout.isErrorEnabled = false
        true
    }

    private fun validateDoseInterval() = if (binding.doseInterval.text.toString().isEmpty()) {
        binding.doseIntervalTextInputLayout.error = getString(R.string.error_required)
        false
    } else if (binding.doseInterval.text.toString().toInt() <= 0) {
        binding.doseIntervalTextInputLayout.error =
            getString(R.string.error_dose_interval_lesser_equal_0)
        false
    } else {
        binding.doseIntervalTextInputLayout.isErrorEnabled = false
        true
    }

    private fun validateTime() = if (binding.doseTime.text.toString().isEmpty()) {
        binding.doseTimeTextInputLayout.error = getString(R.string.error_required)
        false
    } else {
        binding.doseTimeTextInputLayout.isErrorEnabled = false
        true
    }
}
