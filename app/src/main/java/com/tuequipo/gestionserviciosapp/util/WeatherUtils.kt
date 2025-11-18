package com.tuequipo.gestionserviciosapp.util

// Esta función "traduce" el código de la API a texto
fun getWeatherDescription(code: Int): String {
    return when (code) {
        0 -> "☀️ Despejado"
        1, 2, 3 -> "☁️ Parcialmente Nublado"
        45, 48 -> "🌫️ Niebla"
        51, 53, 55 -> "🌧️ Llovizna"
        61, 63, 65 -> "🌧️ Lluvia"
        66, 67 -> "🥶 Lluvia Helada"
        71, 73, 75 -> "❄️ Nieve"
        80, 81, 82 -> "🌦️ Chubascos"
        95, 96, 99 -> "⛈️ Tormenta"
        else -> "Indefinido"
    }
}