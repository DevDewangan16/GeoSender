package com.example.geosender.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.geosender.ui.data.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

object LocationUtils {
    suspend fun getCurrentLocation(
        context: Context,
        onLocationReceived: (LocationData) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)

            // Check permissions
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw SecurityException("Location permission not granted")
            }

            // Get location with await()
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await() // This is where we use await()

            location?.let { androidLocation ->
                onLocationReceived(
                    LocationData(
                        latitude = androidLocation.latitude,
                        longitude = androidLocation.longitude
                    )
                )
            } ?: run {
                throw Exception("Location unavailable")
            }
        } catch (e: Exception) {
            onError(e)
        }
    }
}