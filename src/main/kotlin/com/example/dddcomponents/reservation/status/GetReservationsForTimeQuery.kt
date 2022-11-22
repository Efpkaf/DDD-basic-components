package com.example.dddcomponents.reservation.status

import com.example.dddcomponents.reservation.TimeRange
import com.example.dddcomponents.reservation.readmodel.ReservationCurrentStatus
import com.trendyol.kediatr.Query

data class GetReservationsForTimeQuery(
    val roomId: String,
    val range: TimeRange
) : Query<ReservationCurrentStatus>
