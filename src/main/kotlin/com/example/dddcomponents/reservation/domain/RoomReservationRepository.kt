package com.example.dddcomponents.reservation.domain

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RoomReservationRepository : CrudRepository<RoomReservationsAggregate, String> {

    @Query("""""")
    fun findRoomByReservation(reservationId: UUID): RoomReservationsAggregate

}
