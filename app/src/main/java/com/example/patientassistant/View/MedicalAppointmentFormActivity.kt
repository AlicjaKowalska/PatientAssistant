package com.example.patientassistant.View

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.R
import com.example.patientassistant.ViewModel.AppointmentViewModel
import com.example.patientassistant.ViewModel.AppointmentViewModelFactory
import com.example.patientassistant.databinding.ActivityMedicalAppointmentFormBinding
import java.text.SimpleDateFormat
import java.util.*


class MedicalAppointmentFormActivity : AppCompatActivity() {
    private val calendar = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    lateinit var appointmentViewModel: AppointmentViewModel
    lateinit var binding: ActivityMedicalAppointmentFormBinding

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.edit_text_Title -> {
                    validateTitle()
                }
                R.id.edit_text_medical_facility -> {
                    validateTextMedicalFacility()
                }
                R.id.edit_text_Date -> {
                    validateDate()
                }
                R.id.edit_text_Time -> {
                    validateTime()
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    private fun validateTitle(): Boolean {
        if (binding.editTextTitle.text.isNullOrEmpty()) {
            binding.textInputLayoutTitle.error = "Enter a title"
            binding.editTextTitle.requestFocus()
            return false
        } else {
            binding.textInputLayoutTitle.isErrorEnabled = false
        }

        return true
    }

    private fun validateTextMedicalFacility(): Boolean {
        if (binding.editTextMedicalFacility.text.isNullOrEmpty()) {
            binding.textInputLayoutMedicalFacility.error = "Enter a medical facility"
            binding.editTextMedicalFacility.requestFocus()
            return false
        } else {
            binding.textInputLayoutMedicalFacility.isErrorEnabled = false
        }

        return true
    }

    private fun validateDate(): Boolean {
        if (binding.editTextDate.text.isNullOrEmpty()) {
            binding.textInputLayoutDate.error = "Enter a date"
            binding.editTextMedicalFacility.requestFocus()
            return false
        } else {
            binding.textInputLayoutDate.isErrorEnabled = false
        }

        return true
    }

    private fun validateTime(): Boolean {
        if (binding.editTextTime.text.isNullOrEmpty()) {
            binding.textInputLayoutTime.error = "Enter a time"
            binding.editTextTime.requestFocus()
            return false
        } else {
            binding.textInputLayoutTime.isErrorEnabled = false
        }

        return true
    }

    private fun isValidate(): Boolean =
        validateTitle() && validateTextMedicalFacility() && validateDate() && validateTime()

    private fun setupListeners() {
        binding.editTextTitle.addTextChangedListener(
            TextFieldValidation(binding.editTextTitle)
        )
        binding.editTextMedicalFacility.addTextChangedListener(
            TextFieldValidation(binding.editTextMedicalFacility)
        )
        binding.editTextDate.addTextChangedListener(
            TextFieldValidation(binding.editTextDate)
        )
        binding.editTextTime.addTextChangedListener(
            TextFieldValidation(binding.editTextTime)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicalAppointmentFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        val appointmentViewModelFactory = AppointmentViewModelFactory(
            (application as PatientApplication).appointmentRepository
        )
        appointmentViewModel = ViewModelProvider(
            this, appointmentViewModelFactory
        )[AppointmentViewModel::class.java]

        binding.editTextDate.setOnClickListener {
            DatePickerDialog(
                this@MedicalAppointmentFormActivity,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
            ).show()
        }
        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInEditText()
        }

        binding.editTextTime.setOnClickListener {
            if (DateFormat.is24HourFormat(this)) {
                TimePickerDialog(
                    this,
                    timeSetListener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }
            else {
                TimePickerDialog(
                    this,
                    timeSetListener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            }
        }

        timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeInEditText()
        }

        binding.buttonSave.setOnClickListener {
            if (isValidate()) {
                val title = binding.editTextTitle.text.toString()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val facility = binding.editTextMedicalFacility.text.toString()
                val hour = calendar.get(Calendar.HOUR)
                val minute = calendar.get(Calendar.MINUTE)

                val newAppointment = Appointment(
                    title = title,
                    day = day,
                    month = month,
                    year = year,
                    facility = facility,
                    hour = hour,
                    minute = minute
                )
                appointmentViewModel.insert(newAppointment)
            }
        }
    }

    private fun updateDateInEditText() {
        val formatPresentDate = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(formatPresentDate, Locale.getDefault())
        binding.editTextDate.setText(simpleDateFormat.format(calendar.time).toString())
    }

    private fun updateTimeInEditText() {
        val formatPresentTime = "hh:mm"
        val simpleDateFormat = SimpleDateFormat(formatPresentTime, Locale.getDefault())
        binding.editTextTime.setText(simpleDateFormat.format(calendar.time).toString())
    }
}
