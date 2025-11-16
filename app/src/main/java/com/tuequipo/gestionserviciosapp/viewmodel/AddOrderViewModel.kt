package com.tuequipo.gestionserviciosapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuequipo.gestionserviciosapp.model.AddOrderFormState
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import java.text.SimpleDateFormat // Importa esto
import java.util.* // Importa esto

class AddOrderViewModel(private val repository: ServiceRepository) : ViewModel() {

    // Usamos un StateFlow para el estado del formulario
    private val _formState = MutableStateFlow(AddOrderFormState())
    val formState = _formState.asStateFlow()

    // Mantenemos la lógica de ubicación separada
    val latitude = mutableStateOf<Double?>(null)
    val longitude = mutableStateOf<Double?>(null)



    // Funciones para actualizar el estado cuando el usuario escribe
    fun onClientNameChange(name: String) {
        _formState.update { currentState ->
            currentState.copy(clientName = name, isClientNameError = false)
        }
    }

    fun onDeviceTypeChange(device: String) {
        _formState.update { currentState ->
            currentState.copy(deviceType = device, isDeviceTypeError = false)
        }
    }

    fun onDescriptionChange(description: String) {
        _formState.update { currentState ->
            currentState.copy(description = description, isDescriptionError = false)
        }
    }


    fun setLocation(lat: Double, lon: Double) {
        latitude.value = lat
        longitude.value = lon
    }

    // Esta función reemplaza a la antigua 'saveOrder'
    fun validateAndSaveOrder(): Boolean {
        val state = _formState.value
        val isClientNameValid = state.clientName.isNotBlank()
        val isDeviceTypeValid = state.deviceType.isNotBlank()
        val isDescriptionValid = state.description.isNotBlank()

        // Si hay errores, actualiza el estado para mostrar los mensajes
        if (!isClientNameValid || !isDeviceTypeValid || !isDescriptionValid) {
            _formState.update {
                it.copy(
                    isClientNameError = !isClientNameValid,
                    isDeviceTypeError = !isDeviceTypeValid,
                    isDescriptionError = !isDescriptionValid
                )
            }
            return false // Indica que el guardado falló
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDateString = sdf.format(Date())

        // Si todo es válido, crea la orden y guárdala
        val newOrder = ServiceOrder(
            clientName = state.clientName,
            deviceType = state.deviceType,
            issueDescription = state.description,
            status = "PENDIENTE", // Cambiado de OrderStatus.PENDIENTE
            creationDate = currentDateString,
            latitude = latitude.value,
            longitude = longitude.value
        )

        viewModelScope.launch {
            repository.insert(newOrder)
        }
        return true // Indica que el guardado fue exitoso
    }
}