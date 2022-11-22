package com.example.dddcomponents.reservation.cqrs.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface ReservationStatusRepository: CrudRepository<ReservationCurrentStatus, UUID> {
    @Query(
        """
        select room from ReservationCurrentStatus room 
        where room.roomId = :roomId 
        and room.status in :status
    """
    )
    fun findForTimeRange(roomId: String, status: List<ReservationStatus>): List<ReservationCurrentStatus>

}
