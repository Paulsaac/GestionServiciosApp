package com.tuequipo.gestionserviciosapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tuequipo.gestionserviciosapp.repository.ServiceRepository

// 1. Cambia el constructor para que pida un Repositorio
class ViewModelFactory(private val repository: ServiceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 2. Pasa el repositorio a cada ViewModel
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AddOrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddOrderViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}