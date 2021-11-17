package com.example.burnestimation.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.burnestimation.datamodel.Patient
import com.example.burnestimation.datamodel.PatientRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class PatientViewModel(private val repository: PatientRepository) : ViewModel() {

    // Using Live Data and caching what allWords returns has several benefits
    // - We can put as observer on the data (instead of polling for changes)
    //   and only update the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allPatients: LiveData<List<Patient>> = repository.allPatients.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(patient: Patient) = viewModelScope.launch {
        Log.d("PatientViewModel", "insert patient")
        repository.insert(patient)
    }

}

class PatientViewModelFactory(private val repository: PatientRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientViewModel::class.java)) {
            Log.d("PatientViewModel", "init")
            @Suppress("UNCHECKED_CAST")
            return PatientViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}