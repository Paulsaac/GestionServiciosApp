package com.tuequipo.gestionserviciosapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Enum para los estados del servicio. Es más seguro que usar Strings.
enum class OrderStatus {
    PENDIENTE,
    EN_PROCESO,
    FINALIZADO
}


// @Entity le dice a Room que cree una tabla para esta clase.
@Entity
data class ServiceOrder(
    // @PrimaryKey indica que 'id' es la clave primaria.
    // autoGenerate = true hace que Room genere el ID automáticamente.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clientName: String,
    val deviceType: String,
    val issueDescription: String,
    val status: OrderStatus,
    val creationDate: Date,
    val technicianName: String? = null
)