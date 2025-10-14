package com.tuequipo.gestionserviciosapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuequipo.gestionserviciosapp.database.ServiceOrderDao
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(private val dao: ServiceOrderDao) : ViewModel() {

    private val _order = MutableStateFlow<ServiceOrder?>(null)
    val order: StateFlow<ServiceOrder?> = _order

    // Esta función será llamada desde la UI para cargar los datos de la orden.
    fun loadOrder(id: Int) {
        viewModelScope.launch {
            _order.value = dao.getOrderById(id)
        }
    }

    fun updateOrderStatus(newStatus: OrderStatus) {
        // Obtenemos la orden actual
        _order.value?.let { currentOrder ->
            // Creamos una copia de la orden con el nuevo estado
            val updatedOrder = currentOrder.copy(status = newStatus)
            viewModelScope.launch {
                dao.update(updatedOrder)
                // Actualizamos el estado local para que la UI se refresque inmediatamente
                _order.value = updatedOrder
            }
        }
    }

    fun deleteOrder() {
        _order.value?.let { currentOrder ->
            viewModelScope.launch {
                dao.delete(currentOrder)
            }
        }
    }
}