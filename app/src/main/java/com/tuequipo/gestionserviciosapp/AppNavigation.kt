package com.tuequipo.gestionserviciosapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tuequipo.gestionserviciosapp.database.AppDatabase
import com.tuequipo.gestionserviciosapp.screens.AddOrderScreen
import com.tuequipo.gestionserviciosapp.screens.HomeScreen
import com.tuequipo.gestionserviciosapp.screens.LoginScreen
import com.tuequipo.gestionserviciosapp.screens.OrderDetailScreen
import com.tuequipo.gestionserviciosapp.viewmodel.ViewModelFactory
import com.tuequipo.gestionserviciosapp.network.WeatherApiClient
import com.tuequipo.gestionserviciosapp.network.RetrofitClient // <-- Importa el cliente
import com.tuequipo.gestionserviciosapp.repository.ServiceRepository

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // 1. Obtenemos el cliente para nuestro backend
    val apiService = RetrofitClient.instance
    // 2. Obtenemos el cliente para la API externa del clima
    val weatherApiService = WeatherApiClient.instance
    // 3. Creamos el Repositorio y le pasamos AMBOS servicios
    val repository = ServiceRepository(apiService, weatherApiService)

    val viewModelFactory = ViewModelFactory(repository)

    NavHost(
        navController = navController,
        startDestination = "login" // <-- ESTE TEXTO...
    ) {
        composable(route = "login") { // <-- ...DEBE SER IDÉNTICO A ESTE.
            LoginScreen(navController = navController)
        }
        composable(route = "home") {
            HomeScreen(navController = navController, factory = viewModelFactory)
        }
        composable(route = "add_order") {
            AddOrderScreen(navController = navController, factory = viewModelFactory)
        }
        composable(
            route = "order_detail/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId")
            // Pasa la factory a la pantalla de detalles
            OrderDetailScreen(navController = navController, orderId = orderId, factory = viewModelFactory)
        }
    }
}