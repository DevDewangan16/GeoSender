package com.example.geosender.ui.data

import retrofit2.Response

class LocationRepository(apiService: ApiService) {
    private val apiService = ApiClient.apiService

    suspend fun submitLocation(location: LocationData): Response<ApiResponse> {
        return apiService.submitLocation(location)
    }
}