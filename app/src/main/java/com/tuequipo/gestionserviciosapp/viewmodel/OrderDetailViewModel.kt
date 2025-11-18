package com.tuequipo.gestionserviciosapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.repository.ServiceRepository
import com.tuequipo.gestionserviciosapp.util.getWeatherDescription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class OrderDetailViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _order = MutableStateFlow<ServiceOrder?>(null)
    val order: StateFlow<ServiceOrder?> = _order

    private val _weatherInfo = MutableStateFlow<String?>(null)
    val weatherInfo: StateFlow<String?> = _weatherInfo

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    // --- FUNCIÓN CORREGIDA ---
    // Ahora acepta (id: Int, context: Context)
    fun loadOrder(id: Int, context: Context) {
        viewModelScope.launch {
            try {
                val loadedOrder = repository.getOrderById(id)
                _order.value = loadedOrder

                // Si la orden tiene un nombre de archivo guardado...
                loadedOrder?.imagePath?.let { fileName ->
                    // ...lo buscamos en nuestro directorio de archivos
                    _imageUri.value = getUriFromFileName(context, fileName)
                }

                // Cargar clima (lógica existente)
                loadedOrder?.latitude?.let { lat ->
                    loadedOrder.longitude?.let { lon ->
                        try {
                            val weather = repository.getWeather(lat, lon)
                            val description = getWeatherDescription(weather.currentWeather.weatherCode)
                            val temp = weather.currentWeather.temperature
                            _weatherInfo.value = "$temp°C - $description"
                        } catch (e: Exception) {
                            _weatherInfo.value = "No se pudo obtener el clima"
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _order.value = null
            }
        }
    }

    // Esta función crea un URI para un archivo nuevo en nuestro directorio 'files'
    fun getUriForNewImage(context: Context): Uri {
        val file = File(context.filesDir, "IMG_${System.currentTimeMillis()}.png")
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    // Guarda el path de la imagen en la DB
    fun saveImageUriToOrder(uri: Uri) {
        _order.value?.let { currentOrder ->
            val fileName = uri.lastPathSegment
            if (fileName != null) {
                val updatedOrder = currentOrder.copy(imagePath = fileName)
                viewModelScope.launch {
                    repository.update(updatedOrder)
                    _order.value = updatedOrder
                    _imageUri.value = uri
                }
            }
        }
    }

    // Convierte un nombre de archivo guardado en un URI visible
    private fun getUriFromFileName(context: Context, fileName: String): Uri {
        val file = File(context.filesDir, fileName)
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    // --- Funciones de update/delete (se mantienen igual) ---
    fun updateOrderStatus(newStatus: String) {
        _order.value?.let { currentOrder ->
            val updatedOrder = currentOrder.copy(status = newStatus)
            viewModelScope.launch {
                repository.update(updatedOrder)
                _order.value = updatedOrder
            }
        }
    }

    fun deleteOrder() {
        _order.value?.let { currentOrder ->
            viewModelScope.launch {
                repository.delete(currentOrder)
            }
        }
    }
}