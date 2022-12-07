package com.example.patientassistant.ViewModel

import androidx.lifecycle.*
import com.example.patientassistant.Model.Drug
import com.example.patientassistant.Repository.DrugRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrugViewModel(private val drugRepository: DrugRepository) : ViewModel() {

        fun insert(drug: Drug) = viewModelScope.launch(Dispatchers.IO) {
            drugRepository.insert(drug)
        }
}

class DrugViewModelFactory(private var repository: DrugRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DrugViewModel::class.java)) {
            return DrugViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("unknown View Model")
        }
    }
}
