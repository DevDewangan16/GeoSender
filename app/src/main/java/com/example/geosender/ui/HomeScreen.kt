package com.example.geosender.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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

    // State for permission dialog
    var showPermissionDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.clearMessages()
    }

    // Handle permission request when triggered
    if (showPermissionDialog) {
        RequestLocationPermission(
            onPermissionGranted = {
                coroutineScope.launch {
                    try {
                        LocationUtils.getCurrentLocation(
                            context = context,
                            onLocationReceived = { loc ->
                                viewModel.setLocation(loc)
                            },
                            onError = { error ->
                                viewModel.setErrorMsg(error.message.toString())
                            }
                        )
                    } catch (e: Exception) {
                        viewModel.setErrorMsg(e.message.toString())
                    }
                }
            },
            onPermissionDenied = {
                viewModel.setErrorMsg("Location permission denied")
                showPermissionDialog = false
            },
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

            LocationButton(
                text = "Use Current Location",
                icon = Icons.Default.LocationOn,
                onClick = {
                    showPermissionDialog = true
                }
            )

            LocationButton(
                text = "Select on Map",
                icon = Icons.Default.LocationOn,
                onClick = onNavigateToMap
            )

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