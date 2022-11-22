package com.example.dddcomponents.reservation.readmodel

import org.springframework.data.repository.findByIdOrNull

data class ReservationCurrentStatusService(
    val reservationStatusRepository: ReservationStatusRepository
) {

    fun getStatus(id: String): ReservationCurrentStatus? {
        return reservationStatusRepository.findByIdOrNull(id)
    }
}
