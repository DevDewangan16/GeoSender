package com.example.geosender.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import android.Manifest
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    permission: String = Manifest.permission.ACCESS_FINE_LOCATION,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    val permissionState = rememberPermissionState(permission)
    var showRationale by remember { mutableStateOf(false) }

    LaunchedEffect(permissionState.status) {
        when {
            permissionState.status.isGranted -> {
                onPermissionGranted()
            }
            permissionState.status.shouldShowRationale -> {
                showRationale = true
            }
            else -> {
                permissionState.launchPermissionRequest()
            }
        }
    }

    if (showRationale) {
        AlertDialog(
            onDismissRequest = { showRationale = false },
            title = { Text("Location Permission Needed") },
            text = { Text("This app needs location permission to work properly") },
            confirmButton = {
                Button(onClick = {
                    permissionState.launchPermissionRequest()
                    showRationale = false
                }) {
                    Text("Allow")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRationale = false
                    onPermissionDenied()
                }) {
                    Text("Deny")
                }
            }
        )
    }
}


//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun RequestLocationPermission(
//    permission: String = Manifest.permission.ACCESS_FINE_LOCATION,
//    onPermissionGranted: () -> Unit,
//    onPermissionDenied: () -> Unit,
//    onShowRationale: () -> Unit = {}
//) {
//    // Get the permission state
//    val permissionState = rememberPermissionState(permission)
//
//    LaunchedEffect(permissionState) {
//        when {
//            permissionState.status.isGranted -> {
//                onPermissionGranted()
//            }
//            permissionState.status.shouldShowRationale -> {
//                onShowRationale()
//                onPermissionDenied()
//            }
//            else -> {
//                // Launch permission request
//                permissionState.launchPermissionRequest()
//            }
//        }
//    }
//}