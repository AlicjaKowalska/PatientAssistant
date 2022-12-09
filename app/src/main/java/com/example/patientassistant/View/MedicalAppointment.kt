package com.example.patientassistant.View

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.PatientApplication
import com.example.patientassistant.R
import com.example.patientassistant.ViewModel.AppointmentViewModel
import com.example.patientassistant.ViewModel.AppointmentViewModelFactory
import kotlinx.android.synthetic.main.activity_medical_apointment.*
import java.text.SimpleDateFormat
import java.util.*

class MedicalAppointment : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    lateinit var appointmentViewModel: AppointmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_apointment)

        edit_text_Date.setOnClickListener {
            DatePickerDialog(
                this@MedicalAppointment,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH)).show()
        }
        dateSetListener = DatePickerDialog.OnDateSetListener {
                _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInEditText()
        }

        edit_text_Time.setOnClickListener {
            Toast.makeText(this@MedicalAppointment, "click", Toast.LENGTH_SHORT).show()
            TimePickerDialog(
                this@MedicalAppointment,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        timeSetListener = TimePickerDialog.OnTimeSetListener {
                _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeInEditText()
        }

        button_save.setOnClickListener {
            if (edit_text_Date.text.isNullOrEmpty())
                Toast.makeText(
                    this@MedicalAppointment,
                    "Please enter a title",
                    Toast.LENGTH_SHORT).show()
            else if (edit_text_medical_facility.text.isNullOrEmpty())
                Toast.makeText(
                    this@MedicalAppointment,
                    "Please enter a medical facility",
                    Toast.LENGTH_SHORT).show()
            else if (edit_text_Date.text.isNullOrEmpty())
                Toast.makeText(
                    this@MedicalAppointment,
                    "Please enter a date",
                    Toast.LENGTH_SHORT).show()
            else if (edit_text_Time.text.isNullOrEmpty())
                Toast.makeText(
                    this@MedicalAppointment,
                    "Please enter a time",
                    Toast.LENGTH_SHORT).show()
            else {
                val title = edit_text_Title.text.toString()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val facility = edit_text_medical_facility.toString()

                val newAppointment = Appointment(title, day, month, year, facility)
                insertToDatabase(newAppointment)
            }
        }
    }

    private fun updateDateInEditText() {
        val formatPresentDate = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(formatPresentDate, Locale.getDefault())
        edit_text_Date.setText(simpleDateFormat.format(calendar.time).toString())
    }

    private fun updateTimeInEditText() {
        val formatPresentTime = "hh:mm"
        val simpleDateFormat = SimpleDateFormat(formatPresentTime, Locale.getDefault())
        edit_text_Time.setText(simpleDateFormat.format(calendar.time).toString())
    }

    private fun insertToDatabase(appointment: Appointment) {
        val appointmentViewModelFactory = AppointmentViewModelFactory(
            (application as PatientApplication).appointmentRepository)
        appointmentViewModel = ViewModelProvider(
            this, appointmentViewModelFactory)[AppointmentViewModel::class.java]

        appointmentViewModel.insert(appointment)

        Toast.makeText(this@MedicalAppointment, "toast from fun", Toast.LENGTH_SHORT).show()
    }


}