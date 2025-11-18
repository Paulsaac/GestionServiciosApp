package com.tuequipo.gestionserviciosapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // IMPORTANTE: Esta es la IP especial para que el emulador
    // de Android pueda "ver" el localhost de tu computadora.
    // ¡Nunca uses "localhost:8080" aquí!
    private const val BASE_URL = "http://192.168.1.106:8080/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}