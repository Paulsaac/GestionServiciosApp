package com.tuequipo.gestionserviciosapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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

    val latitude: Double? = null,
    val longitude: Double? = null,

    val imagePath: String? = null
)