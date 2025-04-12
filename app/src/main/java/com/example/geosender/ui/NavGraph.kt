package com.example.geosender.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geosender.ui.data.ApiClient
import com.example.geosender.ui.data.LocationRepository


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    val application = LocalContext.current.applicationContext as Application
    val repository = LocationRepository(ApiClient.apiService)
    val viewModel: LocationViewModel = viewModel(
        factory = LocationViewModelFactory(application, repository)
    )

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToMap = { navController.navigate("map") }
            )
        }
        composable("map") {
            MapScreen(
                viewModel = viewModel,  // Pass the same ViewModel instance
                onBack = { navController.popBackStack() }
            )
        }
    }
}


//@Composable
//fun NavGraph(
//    navController: NavHostController = rememberNavController()
//) {
//    val application = LocalContext.current.applicationContext as Application
//    val repository = LocationRepository(ApiClient.apiService)
//
//    NavHost(
//        navController = navController,
//        startDestination = "home"
//    ) {
//        composable("home") {
//            val viewModel: LocationViewModel = viewModel(
//                factory = LocationViewModelFactory(application, repository)
//            )
//            HomeScreen(
//                viewModel = viewModel,
//                onNavigateToMap = { navController.navigate("map") }
//            )
//        }
//        composable("map") {
//            val viewModel: LocationViewModel = viewModel(
//                factory = LocationViewModelFactory(application, repository)
//            )
//            MapScreen(
//                onLocationSelected = { location ->
//                    viewModel.setLocation(location)
//                    navController.popBackStack()
//                },
//                onBack = { navController.popBackStack() }
//            )
//        }
//    }
//}

class LocationViewModelFactory(
    private val application: Application,
    private val repository: LocationRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}