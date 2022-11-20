package com.example.dddcomponents.reservation

import com.example.dddcomponents.sharedKernel.DomainEvent
import com.example.dddcomponents.sharedKernel.DomainService
import java.util.*

@DomainService
object ReservationService {
    fun changeRoom(
        fromRoom: RoomReservationsAggregate,
        toRoom: RoomReservationsAggregate,
        reservationId: UUID
    ) {
        val reservation = fromRoom.reservations
            .find { it.id == reservationId }
            ?: throw NoSuchReservation(reservationId)

        fromRoom.reservations.remove(reservation)
        fromRoom.domainEvents.add(ReservationRemoved(fromRoom.roomId, reservationId))

        toRoom.requestReservation(
            reservation.owner,
            reservation.timeRange,
            reservation.occurrencePolicy
        )
        toRoom.domainEvents.add(ReservationInserted(toRoom.roomId, reservationId))
    }
}

data class ReservationRemoved(val fromRoomId: String, val reservationId: UUID) : DomainEvent
data class ReservationInserted(val toRoomId: String, val reservationId: UUID) : DomainEvent
