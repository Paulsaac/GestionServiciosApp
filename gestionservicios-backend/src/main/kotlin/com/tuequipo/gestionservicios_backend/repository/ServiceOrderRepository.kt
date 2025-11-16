package com.tuequipo.gestionservicios_backend.repository

import com.tuequipo.gestionservicios_backend.model.ServiceOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceOrderRepository : JpaRepository<ServiceOrder, Int> {
    // JpaRepository nos da mágicamente:
    // .findAll(), .findById(id), .save(order), .deleteById(id)
}