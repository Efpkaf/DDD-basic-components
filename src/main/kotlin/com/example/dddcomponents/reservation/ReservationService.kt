package com.example.dddcomponents.reservation

import com.example.dddcomponents.sharedKernel.DomainEvent
import com.example.dddcomponents.sharedKernel.DomainService
import java.util.*

@DomainService
class ReservationService {
    fun changeRoom(
        fromRoom: RoomReservationsAggregate,
        toRoom: RoomReservationsAggregate,
        reservationId: UUID
    ) {
        val reservation = fromRoom.reservations
            .find { it.id == reservationId }
            ?: throw NoSuchReservation(reservationId)

        fromRoom.reservations.remove(reservation)

        toRoom.requestReservation(
            reservation.owner,
            reservation.timeRange,
            reservation.occurrencePolicy
        )

        fromRoom.domainEvents.add(ReservationMoved(fromRoom.roomId, toRoom.roomId, reservationId))
    }
}

data class ReservationMoved(val fromRoomId: String, val toRoomId: String, val reservationId: UUID) : DomainEvent