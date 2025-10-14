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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).serviceOrderDao()
    val viewModelFactory = ViewModelFactory(dao)

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