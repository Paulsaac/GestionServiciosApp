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
import java.text.SimpleDateFormat
import java.util.*

class AddOrderViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _formState = MutableStateFlow(AddOrderFormState())
    val formState = _formState.asStateFlow()

    val latitude = mutableStateOf<Double?>(null)
    val longitude = mutableStateOf<Double?>(null)

    // ... (las funciones onClientNameChange, onDeviceTypeChange, etc. se mantienen igual)
    fun onClientNameChange(name: String) { _formState.update { it.copy(clientName = name, isClientNameError = false) } }
    fun onDeviceTypeChange(device: String) { _formState.update { it.copy(deviceType = device, isDeviceTypeError = false) } }
    fun onDescriptionChange(description: String) { _formState.update { it.copy(description = description, isDescriptionError = false) } }
    fun setLocation(lat: Double, lon: Double) { latitude.value = lat; longitude.value = lon }

    // --- FUNCIÓN DE GUARDADO MODIFICADA ---
    // 1. Ya no devuelve Boolean. Acepta una función 'onSaveSuccess' como parámetro.
    fun validateAndSaveOrder(onSaveSuccess: () -> Unit) {
        val state = _formState.value
        val isClientNameValid = state.clientName.isNotBlank()
        val isDeviceTypeValid = state.deviceType.isNotBlank()
        val isDescriptionValid = state.description.isNotBlank()

        if (!isClientNameValid || !isDeviceTypeValid || !isDescriptionValid) {
            _formState.update {
                it.copy(
                    isClientNameError = !isClientNameValid,
                    isDeviceTypeError = !isDeviceTypeValid,
                    isDescriptionError = !isDescriptionValid
                )
            }
            return // 2. Si la validación falla, simplemente retorna.
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDateString = sdf.format(Date())

        val newOrder = ServiceOrder(
            clientName = state.clientName,
            deviceType = state.deviceType,
            issueDescription = state.description,
            status = "PENDIENTE",
            creationDate = currentDateString,
            latitude = latitude.value,
            longitude = longitude.value
        )

        viewModelScope.launch {
            try {
                repository.insert(newOrder) // 3. Espera a que el guardado termine...
                onSaveSuccess() // 4. ...y SÓLO ENTONCES llama a la función de éxito.
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}