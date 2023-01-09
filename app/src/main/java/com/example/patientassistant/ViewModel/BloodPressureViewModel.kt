package com.example.patientassistant.ViewModel

import androidx.lifecycle.*
import com.example.patientassistant.Model.BloodPressure
import com.example.patientassistant.Repository.BloodPressureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BloodPressureViewModel(private val bloodPressureRepository: BloodPressureRepository) :
    ViewModel() {

    val allBloodPressure: LiveData<List<BloodPressure>> =
        bloodPressureRepository.allBloodPressure.asLiveData()

    fun insert(bloodPressure: BloodPressure) = viewModelScope.launch(Dispatchers.IO) {
        bloodPressureRepository.insert(bloodPressure)
    }

    fun update(bloodPressure: BloodPressure) = viewModelScope.launch(Dispatchers.IO) {
        bloodPressureRepository.update(bloodPressure)
    }

    fun delete(bloodPressure: BloodPressure) = viewModelScope.launch(Dispatchers.IO) {
        bloodPressureRepository.delete(bloodPressure)
    }

    fun deleteAllBloodPressure() =
        viewModelScope.launch(Dispatchers.IO) {
            bloodPressureRepository.deleteAllBloodPressure()
        }
}

class BloodPressureViewModelFactory(private var repository: BloodPressureRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BloodPressureViewModel::class.java)) {
            return BloodPressureViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("unknown View Model")
        }
    }
}