package com.example.geosender.ui.data

class LocationRepository {
    private val apiService = ApiClient.apiService

    suspend fun submitLocation(location: LocationData): Result<ApiResponse> {
        return try {
            val response = apiService.submitLocation(location)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to submit location"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}