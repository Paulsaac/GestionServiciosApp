package com.tuequipo.gestionserviciosapp.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
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
    val viewModel: AddOrderViewModel = viewModel(factory = factory)
    val formState by viewModel.formState.collectAsState()

    val context = LocalContext.current
    var locationText by remember { mutableStateOf("Ubicación no registrada") }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // --- 1. CREAMOS UNA FUNCIÓN SEPARADA PARA OBTENER LA UBICACIÓN ---
    fun fetchLocation() {
        // Hacemos la comprobación de seguridad (requerida por el IDE)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    viewModel.setLocation(location.latitude, location.longitude)
                    locationText = "Ubicación registrada: Lat ${location.latitude}, Lon ${location.longitude}"
                } else {
                    locationText = "No se pudo obtener la ubicación (activa el GPS del emulador)"
                }
            }
        }
    }

    // --- 2. EL LANZADOR DE PERMISOS AHORA SOLO LLAMA A fetchLocation() ---
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                // Permiso concedido, ahora sí obtenemos la ubicación
                fetchLocation()
            } else {
                locationText = "Permiso de ubicación denegado"
            }
        }
    )



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
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = formState.clientName,
                onValueChange = { viewModel.onClientNameChange(it) },
                label = { Text("Nombre del Cliente") },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.isClientNameError,
                supportingText = {
                    if (formState.isClientNameError) {
                        Text("El nombre del cliente es requerido")
                    }
                },
                trailingIcon = {
                    if (formState.isClientNameError) {
                        Icon(Icons.Default.Error, contentDescription = "Error")
                    }
                }
            )

            OutlinedTextField(
                value = formState.deviceType,
                onValueChange = { viewModel.onDeviceTypeChange(it) },
                label = { Text("Tipo de Equipo (ej: Laptop, Teléfono)") },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.isDeviceTypeError,
                supportingText = {
                    if (formState.isDeviceTypeError) {
                        Text("El tipo de equipo es requerido")
                    }
                },
                trailingIcon = {
                    if (formState.isDeviceTypeError) {
                        Icon(Icons.Default.Error, contentDescription = "Error")
                    }
                }
            )

            OutlinedTextField(
                value = formState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Descripción del Problema") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                isError = formState.isDescriptionError,
                supportingText = {
                    if (formState.isDescriptionError) {
                        Text("La descripción es requerida")
                    }
                },
                trailingIcon = {
                    if (formState.isDescriptionError) {
                        Icon(Icons.Default.Error, contentDescription = "Error")
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    // Comprueba si el permiso YA está concedido
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        // Si ya lo tiene, solo obtén la ubicación
                        fetchLocation()

                    } else {
                        // Si no lo tiene, PIDE el permiso
                        locationPermissionLauncher.launch(arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ))
                    }
                }
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Ubicación")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrar Ubicación Actual")
            }
            Text(text = locationText, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // 1. Llama a la nueva función
                    viewModel.validateAndSaveOrder {
                        // 2. Esta lógica ahora SÓLO se ejecuta
                        //    después de que el ViewModel confirma el guardado.
                        navController.previousBackStackEntry?.savedStateHandle?.set("new_order_added", true)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Orden")
            }
        }
    }
}