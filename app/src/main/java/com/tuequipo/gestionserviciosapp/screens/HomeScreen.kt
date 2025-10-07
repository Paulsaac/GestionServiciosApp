package com.tuequipo.gestionserviciosapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.tuequipo.gestionserviciosapp.model.OrderStatus
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.ui.theme.GestionServiciosAppTheme
import com.tuequipo.gestionserviciosapp.viewmodel.HomeViewModel
import com.tuequipo.gestionserviciosapp.viewmodel.ViewModelFactory

import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, factory: ViewModelFactory) {
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val orders by viewModel.serviceOrders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Órdenes de Servicio") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                navController.navigate("order_detail/${order.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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

/*@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    ServiceOrderItem(
        order = ServiceOrder(1, "Cliente de Prueba", "Laptop", "No enciende", OrderStatus.PENDIENTE, Date()),
        navController = navController
    )
}
*/
