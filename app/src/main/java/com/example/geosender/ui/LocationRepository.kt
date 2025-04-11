package com.example.geosender.ui

import com.example.geosender.ui.data.LocationModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import okhttp3.*
import java.io.IOException

class LocationRepository {
    private val client = OkHttpClient()

    fun sendLocation(location: LocationModel, onResult: (Boolean) -> Unit) {
        val json = JSONObject()
        json.put("latitude", location.latitude)
        json.put("longitude", location.longitude)

        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://example.com/api/submit-location")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false)
            }

            override fun onResponse(call: Call, response: Response) {
                onResult(response.isSuccessful)
            }
        })
    }
}