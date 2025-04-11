package com.example.geosender.ui.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/submit-location")
    suspend fun submitLocation(
        @Body location: LocationData
    ): Response<ApiResponse>
}

data class ApiResponse(
    val success: Boolean,
    val message: String
)