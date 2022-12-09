package com.example.patientassistant.ViewModel

import androidx.lifecycle.*
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.Repository.AppointmentRepository
import com.example.patientassistant.View.MedicalAppointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppointmentViewModel(private val appointmentRepository: AppointmentRepository) : ViewModel() {

    fun insert(appointment: Appointment) = viewModelScope.launch(Dispatchers.IO) {
        appointmentRepository.insert(appointment)
    }
}

class AppointmentViewModelFactory(private var repository: AppointmentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppointmentViewModel::class.java)) {
            return AppointmentViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("unknown View Model")
        }
    }
}
