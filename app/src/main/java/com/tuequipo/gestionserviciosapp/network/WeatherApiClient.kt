package com.tuequipo.gestionserviciosapp.network

import com.tuequipo.gestionserviciosapp.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. URL base de la API del clima
private const val WEATHER_API_BASE_URL = "https://api.open-meteo.com/"

// 2. Interfaz del servicio
interface WeatherApiService {
    @GET("v1/forecast?current_weather=true")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): WeatherResponse
}

// 3. Objeto cliente
object WeatherApiClient {
    val instance: WeatherApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(WEATHER_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(WeatherApiService::class.java)
    }
}