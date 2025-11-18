package com.tuequipo.gestionserviciosapp.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.viewmodel.OrderDetailViewModel
import com.tuequipo.gestionserviciosapp.viewmodel.ViewModelFactory
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    orderId: Int?,
    factory: ViewModelFactory
) {
    val viewModel: OrderDetailViewModel = viewModel(factory = factory)
    val order by viewModel.order.collectAsState()
    val weatherInfo by viewModel.weatherInfo.collectAsState()
    val context = LocalContext.current

    // Lógica de Imagen (corregida)
    val imageUri by viewModel.imageUri.collectAsState()
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempCameraUri?.let { viewModel.saveImageUriToOrder(it) }
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                tempCameraUri = viewModel.getUriForNewImage(context)
                cameraLauncher.launch(tempCameraUri)
            }
        }
    )

    // Carga de datos
    LaunchedEffect(key1 = Unit) {
        orderId?.let { viewModel.loadOrder(id = it, context = context) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la Orden") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (order == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                // --- SECCIÓN DE DETALLES ---
                Text("Cliente: ${order!!.clientName}", style = MaterialTheme.typography.titleLarge)
                Text("Equipo: ${order!!.deviceType}", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Estado: ${order!!.status}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 500))
                )
                Text("Descripción: ${order!!.issueDescription}", style = MaterialTheme.typography.bodyLarge)
                Text("Fecha de Creación: ${order!!.creationDate}", style = MaterialTheme.typography.bodySmall)

                order!!.technicianName?.let {
                    Text("Técnico Asignado: $it", style = MaterialTheme.typography.bodyMedium)
                }

                // --- SECCIÓN DE UBICACIÓN Y CLIMA (REINCORPORADA) ---
                if (order!!.latitude != null && order!!.longitude != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Ubicación Registrada:", style = MaterialTheme.typography.titleSmall)
                    Text("Lat: ${order!!.latitude}, Lon: ${order!!.longitude}", style = MaterialTheme.typography.bodyMedium)
                    weatherInfo?.let {
                        Text(
                            text = "Clima: $it",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                // --- FIN DE LA SECCIÓN ---

                Spacer(modifier = Modifier.height(16.dp))

                // --- SECCIÓN DE CÁMARA (CORREGIDA) ---
                AnimatedVisibility(
                    visible = imageUri != null,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Foto del equipo",
                            modifier = Modifier.size(150.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Button(
                    onClick = {
                        val permission = Manifest.permission.CAMERA
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            tempCameraUri = viewModel.getUriForNewImage(context)
                            cameraLauncher.launch(tempCameraUri)
                        } else {
                            permissionLauncher.launch(permission)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cámara")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tomar Foto del Equipo")
                }
                // --- FIN SECCIÓN CÁMARA ---

                Spacer(modifier = Modifier.height(24.dp))

                // --- SECCIÓN CAMBIAR ESTADO ---
                Text("Cambiar Estado:", style = MaterialTheme.typography.titleSmall)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { viewModel.updateOrderStatus("PENDIENTE") }, enabled = order!!.status != "PENDIENTE") { Text("Pendiente") }
                    Button(onClick = { viewModel.updateOrderStatus("EN_PROCESO") }, enabled = order!!.status != "EN_PROCESO") { Text("En Proceso") }
                    Button(onClick = { viewModel.updateOrderStatus("FINALIZADO") }, enabled = order!!.status != "FINALIZADO") { Text("Finalizado") }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- SECCIÓN ELIMINAR ORDEN ---
                Button(
                    onClick = {
                        viewModel.deleteOrder()
                        navController.previousBackStackEntry?.savedStateHandle?.set("order_deleted", true)
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eliminar Orden")
                }
            }
        }
    }
}