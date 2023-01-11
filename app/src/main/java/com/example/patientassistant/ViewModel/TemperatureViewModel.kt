package com.example.patientassistant.ViewModel

import androidx.lifecycle.*
import com.example.patientassistant.Model.Temperature
import com.example.patientassistant.Repository.TemperatureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TemperatureViewModel(private val temperatureRepository: TemperatureRepository) : ViewModel() {

    val allTemperature: LiveData<List<Temperature>> =
        temperatureRepository.allTemperature.asLiveData()

    fun insert(temperature: Temperature) = viewModelScope.launch(Dispatchers.IO) {
        temperatureRepository.insert(temperature)
    }

    fun update(temperature: Temperature) = viewModelScope.launch(Dispatchers.IO) {
        temperatureRepository.update(temperature)
    }

    fun delete(temperature: Temperature) = viewModelScope.launch(Dispatchers.IO) {
        temperatureRepository.delete(temperature)
    }

    fun deleteAllTemperature() = viewModelScope.launch(Dispatchers.IO) {
        temperatureRepository.deleteAllTemperature()
    }
}

class TemperatureViewModelFactory(private var repository: TemperatureRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemperatureViewModel::class.java)) {
            return TemperatureViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("unknown View Model")
        }
    }
}