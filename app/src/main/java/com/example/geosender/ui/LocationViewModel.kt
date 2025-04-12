package com.example.geosender.ui

import android.app.Application
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.geosender.ui.data.LocationData
import com.example.geosender.ui.data.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

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

    fun reverseGeocode(location: LocationData) {
        viewModelScope.launch {
            try {
                val address = withContext(Dispatchers.IO) {
                    Geocoder(getApplication(), Locale.getDefault()).getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )?.firstOrNull()?.getAddressLine(0)
                }

                _locationState.value = location.copy(address = address)
            } catch (e: Exception) {
                Log.e("LocationVM", "Geocoding error", e)
                _locationState.value = location.copy(address = null)
            }
        }
    }

    fun setLocation(location: LocationData) {
        _locationState.value = location
        reverseGeocode(location)
    }

//    fun setLocation(location: LocationData) {
//        _locationState.value = location
//    }

    fun setErrorMsg(errorMessage:String){
        _errorMessage.value=errorMessage
    }

//    fun submitLocation() {
//        _locationState.value?.let { location ->
//            viewModelScope.launch {
//                _isLoading.value = true
//                _errorMessage.value = null
//                _successMessage.value = null
//
//                try {
//                    val response = repository.submitLocation(location)
//                    if (response.isSuccessful) {
//                        _successMessage.value = "Location submitted successfully"
//                    } else {
//                        _errorMessage.value = "Failed to submit location: ${response.errorBody()?.string() ?: response.code().toString()}"
//                    }
//                } catch (e: Exception) {
//                    _errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
//                } finally {
//                    _isLoading.value = false
//                }
//            }
//        } ?: run {
//            _errorMessage.value = "No location selected"
//        }
//    }
fun submitLocation() {
    _locationState.value?.let { location ->
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            try {
                // Option 1: Directly send LocationData (uses Gson converter)
                val response = repository.submitLocation(location)

                // Option 2: If you need specific format
                /*
                val request = mapOf(
                    "latitude" to location.latitude.toString(),
                    "longitude" to location.longitude.toString()
                )
                val response = repository.submitLocation(request)
                */

                if (response.isSuccessful) {
                    _successMessage.value = response.body()?.message ?: "Location submitted successfully"
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = errorBody ?: "Failed to submit location"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
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