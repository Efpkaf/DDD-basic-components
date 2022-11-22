package com.example.dddcomponents.reservation.cqrs.queries.status

import com.example.dddcomponents.reservation.domain.TimeRange
import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationCurrentStatus
import com.trendyol.kediatr.Query

data class GetReservationsForTimeQuery(
    val roomId: String,
    val range: TimeRange
) : Query<ReservationCurrentStatus>
