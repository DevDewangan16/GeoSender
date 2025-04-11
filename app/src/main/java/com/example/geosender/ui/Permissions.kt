package com.example.geosender.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import android.Manifest
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    permission: String = Manifest.permission.ACCESS_FINE_LOCATION,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onShowRationale: () -> Unit = {}
) {
    // Get the permission state
    val permissionState = rememberPermissionState(permission)

    LaunchedEffect(permissionState) {
        when {
            permissionState.status.isGranted -> {
                onPermissionGranted()
            }
            permissionState.status.shouldShowRationale -> {
                onShowRationale()
                onPermissionDenied()
            }
            else -> {
                // Launch permission request
                permissionState.launchPermissionRequest()
            }
        }
    }
}