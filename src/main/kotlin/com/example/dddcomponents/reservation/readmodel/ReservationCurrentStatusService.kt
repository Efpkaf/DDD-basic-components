package com.example.dddcomponents.reservation.readmodel

import com.example.dddcomponents.reservation.TimeRange
import org.springframework.stereotype.Service

@Service
data class ReservationCurrentStatusService(
    val reservationStatusRepository: ReservationStatusRepository
) {

    fun findAllAcceptedForRoom(roomId: String): List<ReservationCurrentStatus> {
        return reservationStatusRepository.findAllAcceptedForRoom(roomId)
    }

    fun getAcceptedReservations(): MutableIterable<ReservationCurrentStatus> {
        return reservationStatusRepository.findAll()
    }

    fun getAcceptedReservationsFor(roomId: String, range: TimeRange): List<ReservationCurrentStatus> {
        return reservationStatusRepository.findAcceptedForTimeRange(roomId, range.timeFrom, range.timeTo)
    }
}
