package com.example.patientassistant.Fragment

import android.app.AlertDialog
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
import com.example.patientassistant.Objects.DrugFormConstants
import com.example.patientassistant.R
import com.example.patientassistant.View.DrugActivity
import com.example.patientassistant.databinding.FragmentDrugFormBinding
import java.text.SimpleDateFormat
import java.util.*

class FragmentDrugForm(private var drug: Drug?) : Fragment(), TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentDrugFormBinding
    private lateinit var drugActivity: DrugActivity
    private var variant = 0
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrugFormBinding.inflate(inflater, container, false)
        variant = requireArguments().getInt(DrugFormConstants.FORM_VARIANT_KEY, DrugFormConstants.FORM_VARIANT_ADD)
        when (variant) {
            DrugFormConstants.FORM_VARIANT_ADD ->
                setAddingView()
            DrugFormConstants.FORM_VARIANT_DETAILS ->
                setDetailsView()
            DrugFormConstants.FORM_VARIANT_EDIT ->
                setEditView()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drugActivity = activity as DrugActivity
        setupListeners()
    }

    private fun setupListeners() {
        binding.drugName.addTextChangedListener(TextFieldValidation(binding.drugName))
        binding.dose.addTextChangedListener(TextFieldValidation(binding.dose))
        binding.doseInterval.addTextChangedListener(TextFieldValidation(binding.doseInterval))
        binding.doseTime.addTextChangedListener(TextFieldValidation(binding.doseTime))

        setupDoseTimeListener()
    }

    private fun setAddingView() {
        with(binding) {
            buttonEdit.visibility = View.GONE
            buttonDelete.visibility = View.GONE
            buttonSubmitDrugForm.visibility = View.VISIBLE
            tvDrugFormTitle.text = getString(R.string.drug_add_new_title)
            drugName.isEnabled = true
            dose.isEnabled = true
            doseInterval.isEnabled = true
            doseTime.isEnabled = true

            buttonSubmitDrugForm.setOnClickListener {
                if (!validateDrugName() || !validateDose() || !validateDoseInterval() || !validateTime()) {
                    return@setOnClickListener
                }
                saveDrug()
            }
        }
    }

    private fun setDetailsView() {
        with(binding) {
            val textColor = drugName.currentTextColor
            buttonEdit.visibility = View.VISIBLE
            buttonEdit.text = getString(R.string.edit)
            buttonDelete.visibility = View.VISIBLE
            buttonSubmitDrugForm.visibility = View.GONE
            tvDrugFormTitle.text = getString(R.string.drug_details_title)
            drugName.isEnabled = false
            drugName.setTextColor(textColor)
            dose.isEnabled = false
            dose.setTextColor(textColor)
            doseInterval.isEnabled = false
            doseInterval.setTextColor(textColor)
            doseTime.isEnabled = false
            doseTime.setTextColor(textColor)

            buttonEdit.setOnClickListener {
                setEditView()
            }

            buttonDelete.setOnClickListener {
                drug?.let {
                    val deleteDialogBuilder = AlertDialog.Builder(activity).apply {
                        setTitle(R.string.drug_delete_dialog_title)
                        setMessage(R.string.drug_delete_dialog_message)
                        setPositiveButton(R.string.delete) { _, _ ->
                            drugActivity.drugViewModel.delete(it)
                            drugActivity.supportFragmentManager.popBackStack()
                        }
                        setNegativeButton(R.string.cancel) { dialog, _ ->
                            dialog.cancel()
                        }
                    }
                    deleteDialogBuilder.show()
                } ?: throw IllegalStateException("drug cannot be null")
            }
        }
        printDrugInfo()
    }

    private fun setEditView() {
        with(binding) {
            buttonEdit.visibility = View.VISIBLE
            buttonEdit.text = getString(R.string.cancel)
            buttonDelete.visibility = View.VISIBLE
            buttonSubmitDrugForm.visibility = View.VISIBLE
            tvDrugFormTitle.text = getString(R.string.drug_edit_title)
            drugName.isEnabled = true
            dose.isEnabled = true
            doseInterval.isEnabled = true
            doseTime.isEnabled = true

            buttonSubmitDrugForm.setOnClickListener {
                if (!validateDrugName() || !validateDose() || !validateDoseInterval() || !validateTime()) {
                    return@setOnClickListener
                }
                editDrug()
            }

            buttonEdit.setOnClickListener {
                setDetailsView()
            }
        }
    }

    private fun printDrugInfo() {
        drug?.let {
            calendar.set(Calendar.HOUR_OF_DAY, it.hour)
            calendar.set(Calendar.MINUTE, it.minute)
        } ?: throw IllegalStateException("drug cannot be null")
        binding.drugName.setText(drug?.name)
        binding.dose.setText(drug?.dose.toString())
        binding.doseInterval.setText(drug?.interval.toString())
        displayFormattedDate(calendar.timeInMillis)
    }

    private fun setupDoseTimeListener() {
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
        drugActivity.drugViewModel.insert(drug)
        drugActivity.supportFragmentManager.popBackStack()
    }

    private fun editDrug() {
        drug?.let {
            it.name = binding.drugName.text.toString()
            it.dose = binding.dose.text.toString().toInt()
            it.interval = binding.doseInterval.text.toString().toInt()
            it.hour = calendar.get(Calendar.HOUR_OF_DAY)
            it.minute = calendar.get(Calendar.MINUTE)
            drugActivity.drugViewModel.update(it)
            drugActivity.supportFragmentManager.popBackStack()
        } ?: throw IllegalStateException("drug cannot be null")
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
