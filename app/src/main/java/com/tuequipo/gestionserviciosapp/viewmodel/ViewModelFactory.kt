package com.tuequipo.gestionserviciosapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tuequipo.gestionserviciosapp.database.ServiceOrderDao

class ViewModelFactory(private val dao: ServiceOrderDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        if (modelClass.isAssignableFrom(AddOrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddOrderViewModel(dao) as T
        }
        // --- AÑADE ESTE NUEVO BLOQUE ---
        if (modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderDetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}