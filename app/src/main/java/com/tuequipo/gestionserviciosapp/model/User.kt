package com.tuequipo.gestionserviciosapp.model

enum class UserRole {
    CLIENTE,
    TECNICO
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val password: String
)