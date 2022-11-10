package com.example.dddcomponents.request

import java.util.*

interface ReservationRequestRepository {
    fun save(reservationRequest: ReservationRequest)
    fun findById(id: UUID)
}