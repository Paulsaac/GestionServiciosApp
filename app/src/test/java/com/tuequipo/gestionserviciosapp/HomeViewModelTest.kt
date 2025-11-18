package com.tuequipo.gestionserviciosapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import com.tuequipo.gestionserviciosapp.repository.ServiceRepository
import com.tuequipo.gestionserviciosapp.viewmodel.HomeViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    // Regla para que las pruebas de StateFlow funcionen instantáneamente
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // 1. Declarar las variables que necesitamos
    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: ServiceRepository
    private val testDispatcher = StandardTestDispatcher()

    // 2. Configuración (se ejecuta ANTES de cada test)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Establece el hilo principal de Coroutines para la prueba
        repository = mockk() // Crea un "simulacro" de nuestro repositorio
    }

    // 3. Limpieza (se ejecuta DESPUÉS de cada test)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Limpia el hilo principal
    }

    // 4. El Test
    @Test
    fun `test loadOrders() successfully fetches orders`() = runTest {
        // ARRANGE (Organizar)
        // Creamos una lista falsa de órdenes
        val mockOrders = listOf(
            ServiceOrder(1, "Cliente 1", "Laptop", "rota", "PENDIENTE", "17/11/2025", null, null, null)
        )

        // Le decimos al repositorio simulado:
        // "Cuando alguien llame a 'getAllOrders()', devuelve la lista falsa"
        coEvery { repository.getAllOrders() } returns flowOf(mockOrders)

        // ACT (Actuar)
        // Creamos el ViewModel (esto llamará a 'init' y 'loadOrders')
        viewModel = HomeViewModel(repository)

        // Avanzamos el despachador para que las corrutinas se completen
        testDispatcher.scheduler.advanceUntilIdle()

        // ASSERT (Confirmar)
        // Confirmamos que el ViewModel realmente llamó a la función 'getAllOrders' del repositorio
        coVerify { repository.getAllOrders() }

        // Confirmamos que el StateFlow del ViewModel ahora contiene la lista falsa
        assertEquals(mockOrders, viewModel.serviceOrders.value)
    }
}