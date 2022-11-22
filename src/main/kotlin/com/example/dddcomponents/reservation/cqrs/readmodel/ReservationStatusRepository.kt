package com.example.dddcomponents.reservation.cqrs.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ReservationStatusRepository: CrudRepository<ReservationCurrentStatus, String> {
    @Query(
        """
        select room from ReservationCurrentStatus room 
        where room.roomId = :roomId 
        and room.timeFrom >= :timeFrom and room.timeTo <= :timeTo 
        and room.status in :status
    """
    )
    fun findForTimeRange(roomId: String, status: List<ReservationStatus>, timeFrom: Instant, timeTo: Instant): List<ReservationCurrentStatus>

}
