package com.tuequipo.gestionserviciosapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuequipo.gestionserviciosapp.database.ServiceOrderDao
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val dao: ServiceOrderDao) : ViewModel() {
    // Obtenemos el Flow de órdenes desde el DAO y lo convertimos en un StateFlow.
    // La UI "escuchará" a este StateFlow para recibir actualizaciones.
    val serviceOrders: StateFlow<List<ServiceOrder>> = dao.getAllOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}