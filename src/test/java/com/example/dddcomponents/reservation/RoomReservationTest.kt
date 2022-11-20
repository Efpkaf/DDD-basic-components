package com.example.dddcomponents.reservation

import com.example.dddcomponents.reservation.RoomReservationsAggregate.ReservationEntity.OccurrencePolicy
import com.example.dddcomponents.user.Actor
import com.example.dddcomponents.user.ActorType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class RoomReservationTest {
    @Test
    fun givenRoomReservationWhenReservationCreatedAndAcceptedAndCanceledThenCorrectEventsShouldBeSaved() {
        val reservation = RoomReservationsAggregate.createRoom("A102")

        val user = Actor(UUID.randomUUID(), ActorType.USER)
        val admin = Actor(UUID.randomUUID(), ActorType.ADMIN)
        val timeRange = TimeRange.createTimeRange(Instant.ofEpochSecond(1000), Instant.ofEpochSecond(2000))

        val reservationId = reservation.requestReservation(
            user,
            timeRange,
            OccurrencePolicy.OneTimeOccurrence()
        )

        reservation.acceptReservation(
            admin,
            reservationId
        )

        reservation.cancelReservation(
            user,
            reservationId
        )

        Assertions.assertEquals(
            listOf(
                ReservationRequestCreated(reservationId, timeRange, OccurrencePolicy.OneTimeOccurrence()),
                ReservationRequestAccepted(reservationId),
                RequestedToNotifyAdminsAboutCanceledReservation(reservationId, "A102"),
                ReservationCancelled(reservationId)
            ),
            reservation.domainEvents
        )
    }
}