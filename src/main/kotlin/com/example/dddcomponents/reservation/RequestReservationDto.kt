package com.example.dddcomponents.reservation

import com.example.dddcomponents.user.Actor

// TODO: Should failure be signalled by Exception or event?
data class RequestReservationDto(
    val actor: Actor,
    val timeRange: TimeRange,
    val occurencePolicy: RoomReservations.Reservation.OccurrencePolicy
)