package com.tuequipo.gestionservicios_backend.controller

import com.tuequipo.gestionservicios_backend.model.ServiceOrder
import com.tuequipo.gestionservicios_backend.repository.ServiceOrderRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1") // URL Base
class ServiceOrderController(
    private val repository: ServiceOrderRepository
) {

    // GET /api/v1/orders
    @GetMapping("/orders")
    fun getAllOrders(): List<ServiceOrder> {
        return repository.findAll()
    }

    // GET /api/v1/orders/{id}
    @GetMapping("/orders/{id}")
    fun getOrderById(@PathVariable id: Int): ResponseEntity<ServiceOrder> {
        return repository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    // POST /api/v1/orders
    @PostMapping("/orders")
    fun createOrder(@RequestBody order: ServiceOrder): ServiceOrder {
        return repository.save(order)
    }

    // PUT /api/v1/orders/{id}
    @PutMapping("/orders/{id}")
    fun updateOrder(@PathVariable id: Int, @RequestBody orderDetails: ServiceOrder): ResponseEntity<ServiceOrder> {
        return repository.findById(id)
            .map { existingOrder ->
                val updatedOrder = existingOrder.copy(
                    clientName = orderDetails.clientName,
                    deviceType = orderDetails.deviceType,
                    issueDescription = orderDetails.issueDescription,
                    status = orderDetails.status,
                    technicianName = orderDetails.technicianName
                )
                ResponseEntity.ok(repository.save(updatedOrder))
            }
            .orElse(ResponseEntity.notFound().build())
    }

    // DELETE /api/v1/orders/{id}
    @DeleteMapping("/orders/{id}")
    fun deleteOrder(@PathVariable id: Int): ResponseEntity<Void> {
        return repository.findById(id)
            .map { order ->
                repository.delete(order)
                ResponseEntity.ok().build<Void>()
            }
            .orElse(ResponseEntity.notFound().build())
    }
}