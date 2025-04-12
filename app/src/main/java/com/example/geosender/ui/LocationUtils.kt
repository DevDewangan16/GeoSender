package com.example.geosender.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.example.geosender.ui.data.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

object LocationUtils {
    suspend fun getCurrentLocation(
        context: Context,
        onError: (Exception) -> Unit
    ): LocationData? {
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
            onError(SecurityException("Location permission not granted"))
            return null
        }

        // Check if location is enabled
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onError(Exception("Location services are disabled"))
            return null
        }

        return try {
            // Get location with timeout
            val location = withTimeoutOrNull(10_000) {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).await()
            }

            location?.let { androidLocation ->
                LocationData(
                    latitude = androidLocation.latitude,
                    longitude = androidLocation.longitude
                )
            } ?: run {
                onError(Exception("Location unavailable"))
                null
            }
        } catch (e: Exception) {
            onError(e)
            null
        }
    }
}
//object LocationUtils {
//    @SuppressLint("ServiceCast")
//    suspend fun getCurrentLocation(
//        context: Context
//    ): LocationData {
//        val fusedLocationClient: FusedLocationProviderClient =
//            LocationServices.getFusedLocationProviderClient(context)
//
//        // Check permissions
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            throw SecurityException("Location permission not granted")
//        }
//
//        // Check if location is enabled
//        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            throw Exception("Location services are disabled")
//        }
//
//        // Get location
//        val location = fusedLocationClient.getCurrentLocation(
//            Priority.PRIORITY_HIGH_ACCURACY,
//            CancellationTokenSource().token
//        ).await()
//
//        return location?.let { androidLocation ->
//            LocationData(
//                latitude = androidLocation.latitude,
//                longitude = androidLocation.longitude
//            )
//        } ?: throw Exception("Location unavailable")
//    }
//}


//object LocationUtils {
//    suspend fun getCurrentLocation(
//        context: Context,
//        onLocationReceived: (LocationData) -> Unit,
//        onError: (Exception) -> Unit
//    ) {
//        try {
//            val fusedLocationClient: FusedLocationProviderClient =
//                LocationServices.getFusedLocationProviderClient(context)
//
//            // Check permissions
//            if (ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                throw SecurityException("Location permission not granted")
//            }
//
//            // Get location with await()
//            val location = fusedLocationClient.getCurrentLocation(
//                Priority.PRIORITY_HIGH_ACCURACY,
//                CancellationTokenSource().token
//            ).await() // This is where we use await()
//
//            location?.let { androidLocation ->
//                onLocationReceived(
//                    LocationData(
//                        latitude = androidLocation.latitude,
//                        longitude = androidLocation.longitude
//                    )
//                )
//            } ?: run {
//                throw Exception("Location unavailable")
//            }
//        } catch (e: Exception) {
//            onError(e)
//        }
//    }
//}