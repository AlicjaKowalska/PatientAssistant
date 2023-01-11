package com.example.patientassistant.ViewModel

import androidx.lifecycle.*
import com.example.patientassistant.Model.Weight
import com.example.patientassistant.Repository.WeightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeightViewModel(private val weightRepository: WeightRepository) : ViewModel() {

    val allWeight: LiveData<List<Weight>> =
        weightRepository.allWeight.asLiveData()

    fun insert(weight: Weight) = viewModelScope.launch(Dispatchers.IO) {
        weightRepository.insert(weight)
    }

    fun update(weight: Weight) = viewModelScope.launch(Dispatchers.IO) {
        weightRepository.update(weight)
    }

    fun delete(weight: Weight) = viewModelScope.launch(Dispatchers.IO) {
        weightRepository.delete(weight)
    }

    fun deleteAllWeight() = viewModelScope.launch(Dispatchers.IO) {
        weightRepository.deleteAllWeight()
    }
}

class WeightViewModelFactory(private var repository: WeightRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeightViewModel::class.java)) {
            return WeightViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("unknown View Model")
        }
    }
}