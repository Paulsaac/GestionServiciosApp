package com.tuequipo.gestionserviciosapp.model

// Esta data class guardará los valores y los errores de nuestro formulario
data class AddOrderFormState(
    val clientName: String = "",
    val isClientNameError: Boolean = false,
    val deviceType: String = "",
    val isDeviceTypeError: Boolean = false,
    val description: String = "",
    val isDescriptionError: Boolean = false,
)