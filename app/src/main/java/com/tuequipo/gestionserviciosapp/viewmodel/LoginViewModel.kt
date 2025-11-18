package com.tuequipo.gestionserviciosapp.viewmodel

import androidx.lifecycle.ViewModel
import com.tuequipo.gestionserviciosapp.model.User
import com.tuequipo.gestionserviciosapp.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    // 1. Creamos una lista de usuarios simulada (hardcodeada)
    private val mockUsers = listOf(
        User("1", "Administrador", "admin@app.cl", UserRole.TECNICO, "admin123"),
        User("2", "Cliente Prueba", "cliente@app.cl", UserRole.CLIENTE, "cliente123")
    )

    // 2. Creamos un estado para guardar el mensaje de error
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    // 3. Función de Login
    fun login(email: String, pass: String): Boolean {
        // Busca un usuario que coincida con email y contraseña
        val user = mockUsers.find { it.email == email && it.password == pass }

        if (user != null) {
            // Éxito: Limpia el error y devuelve true
            _loginError.value = null
            // TODO: Podríamos guardar el 'user' en un Singleton o DataStore si quisiéramos
            return true
        } else {
            // Falla: Establece un mensaje de error y devuelve false
            _loginError.value = "Credenciales inválidas. Intente de nuevo."
            return false
        }
    }
}