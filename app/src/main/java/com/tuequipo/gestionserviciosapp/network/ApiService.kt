package com.tuequipo.gestionserviciosapp.network

import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import retrofit2.http.*

interface ApiService {

    // Asumiremos que tu API tiene estos endpoints:

    @GET("api/v1/orders")
    suspend fun getAllOrders(): List<ServiceOrder>

    @GET("api/v1/orders/{id}")
    suspend fun getOrderById(@Path("id") id: Int): ServiceOrder

    @POST("api/v1/orders")
    suspend fun createOrder(@Body order: ServiceOrder): ServiceOrder

    @PUT("api/v1/orders/{id}")
    suspend fun updateOrder(@Path("id") id: Int, @Body order: ServiceOrder): ServiceOrder

    @DELETE("api/v1/orders/{id}")
    suspend fun deleteOrder(@Path("id") id: Int)
}