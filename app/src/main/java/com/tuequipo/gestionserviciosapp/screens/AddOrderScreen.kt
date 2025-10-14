package com.tuequipo.gestionserviciosapp.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.tuequipo.gestionserviciosapp.viewmodel.AddOrderViewModel
import com.tuequipo.gestionserviciosapp.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(navController: NavController, factory: ViewModelFactory) {
    val addOrderViewModel: AddOrderViewModel = viewModel(factory = factory)
    val context = LocalContext.current

    // --- CÓDIGO NUEVO PARA UBICACIÓN ---
    var locationText by remember { mutableStateOf("Ubicación no registrada") }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                // Permiso concedido, obtenemos la ubicación
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            addOrderViewModel.setLocation(location.latitude, location.longitude)
                            locationText = "Ubicación registrada: Lat ${location.latitude}, Lon ${location.longitude}"
                        }
                    }
                }
            } else {
                locationText = "Permiso de ubicación denegado"
            }
        }
    )
    // --- FIN CÓDIGO NUEVO ---

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
            OutlinedTextField(
                value = addOrderViewModel.clientName.value,
                onValueChange = { addOrderViewModel.clientName.value = it },
                label = { Text("Nombre del Cliente") },
                modifier = Modifier.fillMaxWidth()
            )
            // ... (Otros OutlinedTextField se mantienen igual)
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
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )

            // --- BOTÓN Y TEXTO DE UBICACIÓN AÑADIDOS ---
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    locationPermissionLauncher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                }
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Ubicación")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrar Ubicación Actual")
            }
            Text(text = locationText, style = MaterialTheme.typography.bodySmall)
            // --- FIN SECCIÓN UBICACIÓN ---

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    addOrderViewModel.saveOrder()
                    navController.previousBackStackEntry?.savedStateHandle?.set("new_order_added", true)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Orden")
            }
        }
    }
}