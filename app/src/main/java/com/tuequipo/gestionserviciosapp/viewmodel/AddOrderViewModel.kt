// En AddOrderViewModel.kt
package com.tuequipo.gestionserviciosapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuequipo.gestionserviciosapp.database.ServiceOrderDao
import com.tuequipo.gestionserviciosapp.model.OrderStatus
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import kotlinx.coroutines.launch
import java.util.Date

// Ahora el ViewModel necesita el DAO para funcionar.
class AddOrderViewModel(private val dao: ServiceOrderDao) : ViewModel() {
    val clientName = mutableStateOf("")
    val deviceType = mutableStateOf("")
    val description = mutableStateOf("")

    fun saveOrder() {
        if (clientName.value.isNotBlank() && deviceType.value.isNotBlank()) {
            val newOrder = ServiceOrder(
                clientName = clientName.value,
                deviceType = deviceType.value,
                issueDescription = description.value,
                status = OrderStatus.PENDIENTE,
                creationDate = Date()
            )

            // Usamos viewModelScope.launch para ejecutar la inserción en un hilo secundario.
            viewModelScope.launch {
                dao.insert(newOrder)
            }
        }
    }
}