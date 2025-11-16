package com.tuequipo.gestionserviciosapp.repository

// Ya no necesitamos el DAO, pero mantenemos la importación del modelo
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// El Repositorio ahora depende de ApiService, no de ServiceOrderDao
class ServiceRepository(private val api: ApiService) {

    // Ya no usamos el DAO local
    // private val dao: ServiceOrderDao

    // Convertimos la lista de la API en un Flow
    fun getAllOrders(): Flow<List<ServiceOrder>> = flow {
        emit(api.getAllOrders())
    }

    // Usamos 'suspend' porque las llamadas de red ya lo son
    suspend fun getOrderById(id: Int): ServiceOrder? {
        return try {
            api.getOrderById(id)
        } catch (e: Exception) {
            null // Devuelve nulo si la API falla (ej: 404)
        }
    }

    suspend fun insert(order: ServiceOrder) {
        api.createOrder(order)
    }

    suspend fun update(order: ServiceOrder) {
        // La API necesita el ID para saber qué actualizar
        api.updateOrder(order.id, order)
    }

    suspend fun delete(order: ServiceOrder) {
        api.deleteOrder(order.id)
    }
}