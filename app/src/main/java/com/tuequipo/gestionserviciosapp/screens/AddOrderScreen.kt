package com.tuequipo.gestionserviciosapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.tuequipo.gestionserviciosapp.ui.theme.GestionServiciosAppTheme
import com.tuequipo.gestionserviciosapp.database.AppDatabase
import com.tuequipo.gestionserviciosapp.viewmodel.AddOrderViewModel
import com.tuequipo.gestionserviciosapp.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(navController: NavController, factory: ViewModelFactory) {
    val addOrderViewModel: AddOrderViewModel = viewModel(factory = factory)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Nueva Orden") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Cada TextField está vinculado a un estado en el ViewModel.
            OutlinedTextField(
                value = addOrderViewModel.clientName.value,
                onValueChange = { addOrderViewModel.clientName.value = it },
                label = { Text("Nombre del Cliente") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = addOrderViewModel.deviceType.value,
                onValueChange = { addOrderViewModel.deviceType.value = it },
                label = { Text("Tipo de Equipo (ej: Laptop, Teléfono)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = addOrderViewModel.description.value,
                onValueChange = { addOrderViewModel.description.value = it },
                label = { Text("Descripción del Problema") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Button(
                onClick = {
                    addOrderViewModel.saveOrder()
                    navController.popBackStack() // Vuelve a la pantalla anterior
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Orden")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddOrderScreenPreview() {
    val context = LocalContext.current
    val dummyDao = AppDatabase.getDatabase(context).serviceOrderDao()
    val dummyFactory = ViewModelFactory(dummyDao)
    GestionServiciosAppTheme {
        AddOrderScreen(navController = rememberNavController(), factory = dummyFactory)
    }
}