package com.example.patientassistant.ViewModel

import androidx.lifecycle.*
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.Repository.AppointmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppointmentViewModel(private val appointmentRepository: AppointmentRepository) : ViewModel() {

    val myAllAppointments : LiveData<List<Appointment>> = appointmentRepository.myAllAppointments.asLiveData()

    fun insert(appointment: Appointment) = viewModelScope.launch(Dispatchers.IO) {
        appointmentRepository.insert(appointment)
    }

    fun update(appointment: Appointment) = viewModelScope.launch(Dispatchers.IO) {
        appointmentRepository.update(appointment)
    }

    fun delete(appointment: Appointment) = viewModelScope.launch(Dispatchers.IO) {
        appointmentRepository.delete(appointment)
    }

    fun deleteAllAppointments(appointment: Appointment) = viewModelScope.launch(Dispatchers.IO) {
        appointmentRepository.deleteAllAppointments()
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
