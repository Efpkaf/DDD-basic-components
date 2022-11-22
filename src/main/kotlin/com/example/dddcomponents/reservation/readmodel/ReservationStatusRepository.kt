package com.example.dddcomponents.reservation.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ReservationStatusRepository: CrudRepository<ReservationCurrentStatus, String> {

    @Query("""
        select room from ReservationCurrentStatus room where room.roomId = :roomId
    """)
    fun findAllAcceptedForRoom(roomId: String): List<ReservationCurrentStatus>

    @Query(
        """
        select room from ReservationCurrentStatus room where room.roomId = :roomId and room.timeFrom >= :from and room.timeTo <= :to and room.status = 'ACCEPTED'
    """
    )
    fun findAcceptedForTimeRange(roomId: String, from: Instant, to: Instant): ReservationCurrentStatus

}
