package com.tuequipo.gestionserviciosapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceOrderDao {
    // suspend indica que esta función debe ser llamada desde una corrutina (para no bloquear la UI).
    @Insert
    suspend fun insert(order: ServiceOrder)

    // Flow<...> es un flujo de datos. Cada vez que los datos cambien en la tabla,
    // este flujo emitirá la nueva lista automáticamente, ¡actualizando la UI en tiempo real!
    @Query("SELECT * FROM ServiceOrder ORDER BY creationDate DESC")
    fun getAllOrders(): Flow<List<ServiceOrder>>
}