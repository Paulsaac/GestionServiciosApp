package com.tuequipo.gestionserviciosapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.model.OrderStatus
import com.tuequipo.gestionserviciosapp.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _order = MutableStateFlow<ServiceOrder?>(null)
    val order: StateFlow<ServiceOrder?> = _order

    // Esta función será llamada desde la UI para cargar los datos de la orden.
    fun loadOrder(id: Int) {
        viewModelScope.launch {
            _order.value = repository.getOrderById(id) // Usa el repositorio
        }
    }

    fun updateOrderStatus(newStatus: OrderStatus) {
        _order.value?.let { currentOrder ->
            val updatedOrder = currentOrder.copy(status = newStatus)
            viewModelScope.launch {
                repository.update(updatedOrder) // Usa el repositorio
                _order.value = updatedOrder
            }
        }
    }

    fun deleteOrder() {
        _order.value?.let { currentOrder ->
            viewModelScope.launch {
                repository.delete(currentOrder) // Usa el repositorio
            }
        }
    }
}