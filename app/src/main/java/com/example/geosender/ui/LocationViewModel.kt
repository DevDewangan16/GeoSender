package com.example.geosender.ui

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.geosender.ui.data.LocationData
import com.example.geosender.ui.data.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    application: Application,
    private val repository: LocationRepository
) : AndroidViewModel(application) {
    private val _locationState = MutableStateFlow<LocationData?>(null)
    val locationState: StateFlow<LocationData?> = _locationState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    fun setLocation(location: LocationData) {
        _locationState.value = location
    }

    fun setErrorMsg(errorMessage:String){
        _errorMessage.value=errorMessage
    }

    fun submitLocation() {
        _locationState.value?.let { location ->
            viewModelScope.launch {
                _isLoading.value = true
                _errorMessage.value = null
                _successMessage.value = null

                try {
                    val response = repository.submitLocation(location)
                    if (response.isSuccessful) {
                        _successMessage.value = response.body()?.message ?: "Location submitted successfully"
                    } else {
                        _errorMessage.value = "Failed to submit location: ${response.code()}"
                    }
                } catch (e: Exception) {
                    _errorMessage.value = "Error: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}