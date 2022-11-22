package com.example.dddcomponents.reservation.readmodel

import javax.persistence.*

@Entity
class ReservationCurrentStatus(
    @Id var id: String,
    var status: ReservationStatus
) {

    fun cancel() {
        status = ReservationStatus.CANCELLED
    }

    fun accept() {
        status = ReservationStatus.ACCEPTED
    }
}


