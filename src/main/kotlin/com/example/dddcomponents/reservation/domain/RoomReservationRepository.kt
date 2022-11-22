package com.example.dddcomponents.reservation.domain

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RoomReservationRepository : CrudRepository<RoomReservationsAggregate, String> {

    @Query(
        """
        select room from RoomReservationsAggregate room JOIN FETCH room.reservations reservation 
        WHERE reservation.id = :id 
    """
    //
    // ^It is not working and I have no idea why...
    )
    fun findRoomReservationsAggregateByReservationsId(id: UUID ): RoomReservationsAggregate

}
