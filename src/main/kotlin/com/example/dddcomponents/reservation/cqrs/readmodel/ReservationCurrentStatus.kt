package com.example.dddcomponents.reservation.cqrs.readmodel

import java.time.Instant
import javax.persistence.*

@Entity
class ReservationCurrentStatus(
    @Id var roomId: String,
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


