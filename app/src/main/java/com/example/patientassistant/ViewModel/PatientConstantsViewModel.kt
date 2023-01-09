package com.example.patientassistant.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.patientassistant.Model.PatientConstants
import com.example.patientassistant.Repository.PatientConstantsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PatientConstantsViewModel(private val patientConstantsRepository: PatientConstantsRepository) :
    ViewModel() {

    fun insert(patientConstants: PatientConstants) = viewModelScope.launch(Dispatchers.IO) {
        patientConstantsRepository.insert(patientConstants)
    }

    fun update(patientConstants: PatientConstants) = viewModelScope.launch(Dispatchers.IO) {
        patientConstantsRepository.update(patientConstants)
    }

    fun delete(patientConstants: PatientConstants) = viewModelScope.launch(Dispatchers.IO) {
        patientConstantsRepository.delete(patientConstants)
    }
}

class PatientConstantsViewModelFactory(private var repository: PatientConstantsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientConstantsViewModel::class.java)) {
            return PatientConstantsViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("unknown View Model")
        }
    }
}