package com.tuequipo.gestionserviciosapp.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.tuequipo.gestionserviciosapp.model.OrderStatus
import com.tuequipo.gestionserviciosapp.viewmodel.OrderDetailViewModel
import com.tuequipo.gestionserviciosapp.viewmodel.ViewModelFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    orderId: Int?,
    factory: ViewModelFactory
) {
    val viewModel: OrderDetailViewModel = viewModel(factory = factory)
    val order by viewModel.order.collectAsState()
    val context = LocalContext.current

    var hasImage by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", context.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", tmpFile)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success -> hasImage = success }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                imageUri = getTmpFileUri()
                cameraLauncher.launch(imageUri)
            }
        }
    )

    LaunchedEffect(Unit) {
        orderId?.let { viewModel.loadOrder(it) }
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
            // Se añade verticalScroll para evitar que el contenido se desborde en pantallas pequeñas
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text("Cliente: ${order!!.clientName}", style = MaterialTheme.typography.titleLarge)
                Text("Equipo: ${order!!.deviceType}", style = MaterialTheme.typography.titleMedium)
                Text("Estado: ${order!!.status}", style = MaterialTheme.typography.titleMedium)
                Text("Descripción: ${order!!.issueDescription}", style = MaterialTheme.typography.bodyLarge)
                val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(order!!.creationDate)
                Text("Fecha de Creación: $formattedDate", style = MaterialTheme.typography.bodySmall)

                order!!.technicianName?.let {
                    Text("Técnico Asignado: $it", style = MaterialTheme.typography.bodyMedium)
                }

                if (order!!.latitude != null && order!!.longitude != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Ubicación Registrada:", style = MaterialTheme.typography.titleSmall)
                    Text("Lat: ${order!!.latitude}, Lon: ${order!!.longitude}", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (hasImage && imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Foto del equipo",
                        modifier = Modifier.size(150.dp).align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        val permission = Manifest.permission.CAMERA
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            imageUri = getTmpFileUri()
                            cameraLauncher.launch(imageUri)
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

                Spacer(modifier = Modifier.height(24.dp))

                Text("Cambiar Estado:", style = MaterialTheme.typography.titleSmall)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { viewModel.updateOrderStatus(OrderStatus.PENDIENTE) }, enabled = order!!.status != OrderStatus.PENDIENTE) { Text("Pendiente") }
                    Button(onClick = { viewModel.updateOrderStatus(OrderStatus.EN_PROCESO) }, enabled = order!!.status != OrderStatus.EN_PROCESO) { Text("En Proceso") }
                    Button(onClick = { viewModel.updateOrderStatus(OrderStatus.FINALIZADO) }, enabled = order!!.status != OrderStatus.FINALIZADO) { Text("Finalizado") }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- BOTÓN DE ELIMINAR REINCORPORADO ---
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