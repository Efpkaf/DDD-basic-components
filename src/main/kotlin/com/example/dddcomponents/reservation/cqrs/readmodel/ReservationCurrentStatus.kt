package com.example.dddcomponents.reservation.cqrs.readmodel

import org.hibernate.annotations.Type
import java.time.Instant
import java.util.UUID
import javax.persistence.*

@Entity
data class ReservationCurrentStatus(
    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    var id: UUID,
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


