package com.example.geosender.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.geosender.ui.data.LocationData
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onLocationSelected: (LocationData) -> Unit,
    onBack: () -> Unit,
    initialLocation: LocationData? = null
) {
    val initialLatLng = initialLocation?.let {
        LatLng(it.latitude, it.longitude)
    } ?: LatLng(0.0, 0.0)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, 15f)
    }

    var selectedLocation by remember { mutableStateOf<LatLng?>(initialLatLng) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Location") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedLocation?.let { latLng ->
                        onLocationSelected(
                            LocationData(
                                latitude = latLng.latitude,
                                longitude = latLng.longitude
                            )
                        )
                    }
                }
            ) {
                Icon(Icons.Default.Check, contentDescription = "Confirm Location")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = true
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                ),
                onMapClick = { latLng ->
                    selectedLocation = latLng
                }
            ) {
                selectedLocation?.let { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Selected Location"
                    )
                }
            }

            if (selectedLocation == null) {
                Text(
                    text = "Tap on the map to select location",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
//@OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3Api::class)
//@Composable
//fun MapScreen(
//    onLocationSelected: (LocationData) -> Unit,
//    onBack: () -> Unit,
//    initialLocation: LocationData? = null
//) {
//    // Convert initialLocation to LatLng if available
//    val initialLatLng = initialLocation?.let {
//        LatLng(it.latitude, it.longitude)
//    } ?: LatLng(0.0, 0.0) // Default location
//
//    // Camera position state
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(initialLatLng, 15f)
//    }
//
//    // Selected location state
//    var selectedLocation by remember { mutableStateOf<LatLng?>(initialLatLng) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Select Location") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    selectedLocation?.let { latLng ->
//                        onLocationSelected(
//                            LocationData(
//                                latitude = latLng.latitude,
//                                longitude = latLng.longitude
//                            )
//                        )
//                    }
//                },
//                // enabled = selectedLocation != null
//            ) {
//                Icon(Icons.Default.Check, contentDescription = "Confirm Location")
//            }
//        }
//    ) { paddingValues ->
//        Box(modifier = Modifier.padding(paddingValues)) {
//            GoogleMap(
//                modifier = Modifier.fillMaxSize(),
//                cameraPositionState = cameraPositionState,
//                properties = MapProperties(
//                    isMyLocationEnabled = true
//                ),
//                uiSettings = MapUiSettings(
//                    zoomControlsEnabled = true,
//                    myLocationButtonEnabled = true
//                ),
//                onMapClick = { latLng ->
//                    selectedLocation = latLng
//                }
//            ) {
//                selectedLocation?.let { location ->
//                    Marker(
//                        state = MarkerState(position = location),
//                        title = "Selected Location"
//                    )
//                }
//            }
//
//            if (selectedLocation == null) {
//                Text(
//                    text = "Tap on the map to select location",
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .background(
//                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .padding(16.dp),
//                    style = MaterialTheme.typography.bodyLarge
//                )
//            }
//        }
//    }
//}