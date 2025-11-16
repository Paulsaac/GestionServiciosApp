package com.tuequipo.gestionserviciosapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date



// @Entity le dice a Room que cree una tabla para esta clase.
@Entity
data class ServiceOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clientName: String,
    val deviceType: String,
    val issueDescription: String,
    val status: String,
    val creationDate: String,
    val technicianName: String? = null,
    // --- NUEVOS CAMPOS AÑADIDOS ---
    val latitude: Double? = null,
    val longitude: Double? = null
)