package com.example.dddcomponents.reservation.request

import com.example.dddcomponents.reservation.RoomReservationsAggregate
import com.example.dddcomponents.reservation.TimeRange
import com.example.dddcomponents.user.Actor
import com.trendyol.kediatr.Command

data class RequestReservationCommand(
    val roomId: String,
    val actor: Actor,
    val timeRange: TimeRange,
    val occurrencePolicy: RoomReservationsAggregate.ReservationEntity.OccurrencePolicy
) : Command
