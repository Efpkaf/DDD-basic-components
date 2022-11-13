package com.example.dddcomponents.reservation

import com.example.dddcomponents.request.ReservationRequest
import com.example.dddcomponents.sharedKernel.DomainService

@DomainService
class ReservationService {
    fun processReservationRequest(reservationRequest: ReservationRequest, roomReservations: RoomReservations) {
        reservationRequest.applyReservationRequest {
            val possibleReservation = RoomReservations.Reservation.createReservation(reservationRequest.timeRange)
            roomReservations.applyReservation(possibleReservation)
        }
    }
}
