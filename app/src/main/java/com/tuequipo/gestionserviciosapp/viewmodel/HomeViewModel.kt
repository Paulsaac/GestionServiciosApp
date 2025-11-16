package com.tuequipo.gestionserviciosapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ServiceRepository) : ViewModel() {

    // 1. Hacemos la lista "privada" para poder controlarla
    private val _serviceOrders = MutableStateFlow<List<ServiceOrder>>(emptyList())
    val serviceOrders: StateFlow<List<ServiceOrder>> = _serviceOrders.asStateFlow()

    init {
        // 2. Cargamos la lista al iniciar el ViewModel
        loadOrders()
    }

    // 3. Creamos una función pública para "refrescar" la lista
    fun loadOrders() {
        viewModelScope.launch {
            // Usamos un try-catch por si la red falla
            try {
                repository.getAllOrders()
                    .collect { orders ->
                        _serviceOrders.value = orders
                    }
            } catch (e: Exception) {
                // Manejar el error de red (ej: mostrar un log)
                e.printStackTrace()
            }
        }
    }
}