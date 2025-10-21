package com.tuequipo.gestionserviciosapp.repository

import com.tuequipo.gestionserviciosapp.database.ServiceOrderDao
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import kotlinx.coroutines.flow.Flow

// El repositorio necesita el DAO para poder acceder a la base de datos.
class ServiceRepository(private val dao: ServiceOrderDao) {

    // Simplemente reenviamos las peticiones al DAO.
    // Si tuviéramos una fuente de datos en la nube, la lógica iría aquí.

    fun getAllOrders(): Flow<List<ServiceOrder>> {
        return dao.getAllOrders()
    }

    suspend fun getOrderById(id: Int): ServiceOrder? {
        return dao.getOrderById(id)
    }

    suspend fun insert(order: ServiceOrder) {
        dao.insert(order)
    }

    suspend fun update(order: ServiceOrder) {
        dao.update(order)
    }

    suspend fun delete(order: ServiceOrder) {
        dao.delete(order)
    }
}