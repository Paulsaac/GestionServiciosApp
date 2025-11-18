package com.tuequipo.gestionservicios_backend.model

// Importamos desde jakarta, no javax
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
data class ServiceOrder(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val clientName: String,
    val deviceType: String,
    val issueDescription: String,

    val status: String, // PENDIENTE, EN_PROCESO, etc.
    val creationDate: String,
    val technicianName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,

    val imagePath: String? = null
)