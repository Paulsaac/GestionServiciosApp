package com.tuequipo.gestionserviciosapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.viewmodel.HomeViewModel
import com.tuequipo.gestionserviciosapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, factory: ViewModelFactory) {
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val orders by viewModel.serviceOrders.collectAsState()

    // --- CÓDIGO NUEVO PARA SNACKBAR ---
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val newOrderAdded = navController.currentBackStackEntry
        ?.savedStateHandle?.get<Boolean>("new_order_added") ?: false
    val orderDeleted = navController.currentBackStackEntry
        ?.savedStateHandle?.get<Boolean>("order_deleted") ?: false

    // Muestra el Snackbar si se añadió o eliminó una orden
    LaunchedEffect(newOrderAdded, orderDeleted) {
        if (newOrderAdded) {
            scope.launch {
                snackbarHostState.showSnackbar("Orden guardada con éxito")
            }
            navController.currentBackStackEntry?.savedStateHandle?.set("new_order_added", false)
            viewModel.loadOrders()
        }
        if (orderDeleted) {
            scope.launch {
                snackbarHostState.showSnackbar("Orden eliminada correctamente")
            }
            navController.currentBackStackEntry?.savedStateHandle?.set("order_deleted", false)
            viewModel.loadOrders()
        }
    }
    // --- FIN CÓDIGO NUEVO PARA SNACKBAR ---

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, // Añadimos el host del Snackbar
        topBar = {
            TopAppBar(
                title = { Text("Órdenes de Servicio") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_order") }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Servicio")
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(orders) { order ->
                ServiceOrderItem(order = order, navController = navController)
            }
        }
    }
}

@Composable
fun ServiceOrderItem(order: ServiceOrder, navController: NavController) {
    // ... (El código de ServiceOrderItem se mantiene como lo definimos en el paso anterior)
    val cardColor = when (order.status) {
        "PENDIENTE" -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        "EN_PROCESO" -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        "FINALIZADO" -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.3f) // Un default
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                navController.navigate("order_detail/${order.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${order.deviceType} - ${order.clientName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Estado: ${order.status}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}