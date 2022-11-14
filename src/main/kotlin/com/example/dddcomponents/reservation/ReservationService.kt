package com.example.dddcomponents.reservation

import com.example.dddcomponents.request.ReservationRequest
import com.example.dddcomponents.sharedKernel.DomainEvent
import com.example.dddcomponents.sharedKernel.DomainService
import com.example.dddcomponents.user.Actor
import java.util.*

@DomainService
class ReservationService {
    fun processReservationRequest(
        actor: Actor,
        reservationRequest: ReservationRequest,
        roomReservations: RoomReservations
    ): List<DomainEvent> {
        val events = LinkedList<DomainEvent>()

        events += reservationRequest.acceptReservationRequest(actor) {
            val possibleReservation =
                RoomReservations.Reservation.createReservation(actor, reservationRequest.timeRange)
            events += roomReservations.applyReservation(actor, possibleReservation)
        }

        return events
    }
}
