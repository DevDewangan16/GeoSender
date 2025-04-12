package com.example.geosender.ui.data

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/submit-location")
    suspend fun submitLocation(
        @Body location: LocationData
    ): retrofit2.Response<ApiResponse>

    // Or if you need specific format:
    @POST("api/submit-location")
    suspend fun submitLocation(
        @Body request: Map<String, String>
    ): retrofit2.Response<ApiResponse>
}
data class ApiResponse(
    val success: Boolean,
    val message: String
)