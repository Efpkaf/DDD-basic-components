package com.example.dddcomponents.reservation

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

        fromRoom.remove(reservation)

        toRoom.add(reservation)
    }
}
