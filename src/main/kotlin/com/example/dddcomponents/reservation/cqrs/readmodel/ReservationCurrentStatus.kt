package com.example.dddcomponents.reservation.cqrs.readmodel

import java.time.Instant
import java.util.UUID
import javax.persistence.*

@Entity
class ReservationCurrentStatus(
    @Id var id: UUID,
    var roomId: String,
    var status: ReservationStatus,
    val timeFrom: Instant,
    val timeTo: Instant
) {

    fun cancel() {
        status = ReservationStatus.CANCELLED
    }

    fun accept() {
        status = ReservationStatus.ACCEPTED
    }
}


