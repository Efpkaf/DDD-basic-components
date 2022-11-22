package com.example.dddcomponents.reservation.cqrs.queries.list

import com.example.dddcomponents.reservation.domain.TimeRange
import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationCurrentStatus
import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationStatus
import com.trendyol.kediatr.Query

data class GetReservationsForTimeQuery(
    val roomId: String,
    val range: TimeRange,
    val status: List<ReservationStatus>
) : Query<List<ReservationCurrentStatus>>
