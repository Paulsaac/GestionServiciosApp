package com.tuequipo.gestionserviciosapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuequipo.gestionserviciosapp.database.ServiceOrderDao
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.repository.ServiceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val repository: ServiceRepository) : ViewModel() {
    // Obtenemos el Flow de órdenes desde el DAO y lo convertimos en un StateFlow.
    // La UI "escuchará" a este StateFlow para recibir actualizaciones.
    val serviceOrders: StateFlow<List<ServiceOrder>> = repository.getAllOrders() // Usa el repositorio
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}