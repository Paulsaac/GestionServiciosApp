package com.tuequipo.gestionserviciosapp.model

import com.google.gson.annotations.SerializedName

// Este es el objeto principal que recibimos
data class WeatherResponse(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather
)

// Este objeto está anidado dentro de la respuesta
data class CurrentWeather(
    @SerializedName("temperature")
    val temperature: Double,

    @SerializedName("weathercode")
    val weatherCode: Int
)