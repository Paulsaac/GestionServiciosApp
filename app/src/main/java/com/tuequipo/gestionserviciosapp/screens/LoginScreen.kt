package com.tuequipo.gestionserviciosapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tuequipo.gestionserviciosapp.ui.theme.GestionServiciosAppTheme

// @Composable indica que esta es una función de UI
@Composable
fun LoginScreen(navController: NavController) {

    // Usamos estados para guardar lo que el usuario escribe.
    // 'remember' hace que el valor no se pierda si la pantalla se redibuja.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Column apila los elementos verticalmente
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el tamaño de la pantalla
            .padding(16.dp), // Añade un margen de 16dp en todos los lados
        verticalArrangement = Arrangement.Center, // Centra los elementos verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
    ) {
        Text(text = "Bienvenido", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp)) // Un espacio vertical

        // Campo de texto para el email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it }, // Actualiza el estado cuando el usuario escribe
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho disponible
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para la contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de inicio de sesión
        Button(
            onClick = {
                // Le decimos al navController que navegue a la ruta "home"
                navController.navigate("home")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión")
        }
    }
}

// @Preview nos permite ver el diseño sin tener que ejecutar la app
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    GestionServiciosAppTheme {
        // Creamos un NavController de prueba para la vista previa
        val navController = rememberNavController()
        LoginScreen(navController = navController)
    }
}