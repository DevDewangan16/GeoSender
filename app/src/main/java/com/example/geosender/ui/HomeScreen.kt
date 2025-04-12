package com.example.geosender.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.geosender.ui.LocationUtils.getCurrentLocation
import com.example.geosender.ui.data.LocationData
import kotlinx.coroutines.launch
import android.provider.Settings
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat


@Composable
fun HomeScreen(
    viewModel: LocationViewModel,
    onNavigateToMap: () -> Unit
) {
    val context = LocalContext.current
    val location by viewModel.locationState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showPermissionDialog by remember { mutableStateOf(false) }
    var showLocationDisabledDialog by remember { mutableStateOf(false) }
    var isGettingLocation by remember { mutableStateOf(false) }

    // Clear messages when screen loads
    LaunchedEffect(Unit) {
        viewModel.clearMessages()
    }

    // Handle location permission and services
    if (showPermissionDialog) {
        RequestLocationPermission(
            onPermissionGranted = {
                isGettingLocation = true
                coroutineScope.launch {
                    LocationUtils.getCurrentLocation(context) { error ->
                        viewModel.setErrorMsg(error.message ?: "Location error")
                    }?.let { currentLocation ->
                        viewModel.setLocation(currentLocation)
                    }
                    isGettingLocation = false
                }
            },
            onPermissionDenied = {
                viewModel.setErrorMsg("Location permission required")
                showPermissionDialog = false
            }
        )
    }

    // Dialog for when location services are disabled
    if (showLocationDisabledDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDisabledDialog = false },
            title = { Text("Enable Location") },
            text = { Text("Please turn on location services to continue") },
            confirmButton = {
                Button(
                    onClick = {
                        showLocationDisabledDialog = false
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLocationDisabledDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Select Your Location",
                style = MaterialTheme.typography.headlineSmall
            )

            LocationCard(location = location)

            // Use Current Location Button
            if (isGettingLocation) {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                }
            } else {
                LocationButton(
                    text = "Use Current Location",
                    icon = Icons.Default.LocationOn,
                    onClick = {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            // Check if location is enabled
                            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                showLocationDisabledDialog = true
                            } else {
                                isGettingLocation = true
                                coroutineScope.launch {
                                    LocationUtils.getCurrentLocation(context) { error ->
                                        viewModel.setErrorMsg(error.message ?: "Location error")
                                        if (error.message == "Location services are disabled") {
                                            showLocationDisabledDialog = true
                                        }
                                    }?.let { currentLocation ->
                                        viewModel.setLocation(currentLocation)
                                    }
                                    isGettingLocation = false
                                }
                            }
                        } else {
                            showPermissionDialog = true
                        }
                    }
                )
            }

            // Select on Map Button
            LocationButton(
                text = "Select on Map",
                icon = Icons.Default.LocationOn,
                onClick = {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        onNavigateToMap()
                    } else {
                        showPermissionDialog = true
                    }
                }
            )

            // Submit Button
            Button(
                onClick = { viewModel.submitLocation() },
                modifier = Modifier.fillMaxWidth(),
                enabled = location != null && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Submit Location")
                }
            }
        }

        // Snackbars for messages
        errorMessage?.let { msg ->
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                action = {
                    TextButton(onClick = { viewModel.clearMessages() }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text(msg)
            }
        }

        successMessage?.let { msg ->
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                action = {
                    TextButton(onClick = { viewModel.clearMessages() }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text(msg)
            }
        }
    }
}

//@Composable
//fun HomeScreen(
//    viewModel: LocationViewModel,
//    onNavigateToMap: () -> Unit
//) {
//    val context = LocalContext.current
//    val location by viewModel.locationState.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//    val successMessage by viewModel.successMessage.collectAsState()
//    val coroutineScope = rememberCoroutineScope()
//
//    var showPermissionDialog by remember { mutableStateOf(false) }
//    var showLocationDisabledDialog by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        viewModel.clearMessages()
//    }
//
//    // Handle location permission and services
//    if (showPermissionDialog) {
//        RequestLocationPermission(
//            onPermissionGranted = {
//                coroutineScope.launch {
//                    try {
//                        val currentLocation = LocationUtils.getCurrentLocation(context)
//                        viewModel.setLocation(currentLocation)
//                    } catch (e: SecurityException) {
//                        viewModel.setErrorMsg("Location permission denied")
//                    } catch (e: Exception) {
//                        if (e.message == "Location services are disabled") {
//                            showLocationDisabledDialog = true
//                        } else {
//                            viewModel.setErrorMsg(e.message ?: "Failed to get location")
//                        }
//                    }
//                }
//            },
//            onPermissionDenied = {
//                viewModel.setErrorMsg("Location permission denied")
//            }
//        )
//    }
//
//    // Dialog for when location services are disabled
//    if (showLocationDisabledDialog) {
//        AlertDialog(
//            onDismissRequest = { showLocationDisabledDialog = false },
//            title = { Text("Location Services Disabled") },
//            text = { Text("Please enable location services to use this feature") },
//            confirmButton = {
//                Button(onClick = {
//                    showLocationDisabledDialog = false
//                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                }) {
//                    Text("Open Settings")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showLocationDisabledDialog = false }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Text(
//                text = "Select Your Location",
//                style = MaterialTheme.typography.headlineSmall
//            )
//
//            LocationCard(location = location)
//
//            LocationButton(
//                text = "Use Current Location",
//                icon = Icons.Default.LocationOn,
//                onClick = {
//                    showPermissionDialog = true
//                }
//            )
//
//            LocationButton(
//                text = "Select on Map",
//                icon = Icons.Default.LocationOn,
//                onClick = {
//                    // Check permission before navigating to map
//                    if (ContextCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                        ) == PackageManager.PERMISSION_GRANTED
//                    ) {
//                        onNavigateToMap()
//                    } else {
//                        showPermissionDialog = true
//                    }
//                }
//            )
//
//            Button(
//                onClick = { viewModel.submitLocation() },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = location != null && !isLoading
//            ) {
//                if (isLoading) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(24.dp),
//                        color = MaterialTheme.colorScheme.onPrimary,
//                        strokeWidth = 2.dp
//                    )
//                } else {
//                    Text("Submit Location")
//                }
//            }
//        }
//
//        // Show error message if any
//        errorMessage?.let { msg ->
//            Snackbar(
//                modifier = Modifier.align(Alignment.BottomCenter),
//                action = {
//                    TextButton(onClick = { viewModel.clearMessages() }) {
//                        Text("Dismiss")
//                    }
//                }
//            ) {
//                Text(msg)
//            }
//        }
//
//        // Show success message if any
//        successMessage?.let { msg ->
//            Snackbar(
//                modifier = Modifier.align(Alignment.BottomCenter),
//                action = {
//                    TextButton(onClick = { viewModel.clearMessages() }) {
//                        Text("Dismiss")
//                    }
//                }
//            ) {
//                Text(msg)
//            }
//        }
//    }
//}


//@Composable
//fun HomeScreen(
//    viewModel: LocationViewModel,
//    onNavigateToMap: () -> Unit
//) {
//    val context = LocalContext.current
//    val location by viewModel.locationState.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//    val successMessage by viewModel.successMessage.collectAsState()
//    val coroutineScope = rememberCoroutineScope()
//
//    // State for permission dialog
//    var showPermissionDialog by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        viewModel.clearMessages()
//    }
//
//    // Handle permission request when triggered
//    if (showPermissionDialog) {
//        RequestLocationPermission(
//            onPermissionGranted = {
//                coroutineScope.launch {
//                    try {
//                        LocationUtils.getCurrentLocation(
//                            context = context,
//                            onLocationReceived = { loc ->
//                                viewModel.setLocation(loc)
//                            },
//                            onError = { error ->
//                                viewModel.setErrorMsg(error.message.toString())
//                            }
//                        )
//                    } catch (e: Exception) {
//                        viewModel.setErrorMsg(e.message.toString())
//                    }
//                }
//            },
//            onPermissionDenied = {
//                viewModel.setErrorMsg("Location permission denied")
//                showPermissionDialog = false
//            },
//        )
//    }
//
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Text(
//                text = "Select Your Location",
//                style = MaterialTheme.typography.headlineSmall
//            )
//
//            LocationCard(location = location)
//
//            LocationButton(
//                text = "Use Current Location",
//                icon = Icons.Default.LocationOn,
//                onClick = {
//                    showPermissionDialog = true
//                }
//            )
//
//            LocationButton(
//                text = "Select on Map",
//                icon = Icons.Default.LocationOn,
//                onClick = onNavigateToMap
//            )
//
//            Button(
//                onClick = { viewModel.submitLocation() },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = location != null && !isLoading
//            ) {
//                if (isLoading) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(24.dp),
//                        color = MaterialTheme.colorScheme.onPrimary,
//                        strokeWidth = 2.dp
//                    )
//                } else {
//                    Text("Submit Location")
//                }
//            }
//        }
//
//        errorMessage?.let { msg ->
//            Snackbar(
//                modifier = Modifier.align(Alignment.BottomCenter),
//                action = {
//                    TextButton(onClick = { viewModel.clearMessages() }) {
//                        Text("Dismiss")
//                    }
//                }
//            ) {
//                Text(msg)
//            }
//        }
//
//        successMessage?.let { msg ->
//            Snackbar(
//                modifier = Modifier.align(Alignment.BottomCenter),
//                action = {
//                    TextButton(onClick = { viewModel.clearMessages() }) {
//                        Text("Dismiss")
//                    }
//                }
//            ) {
//                Text(msg)
//            }
//        }
//    }
//}