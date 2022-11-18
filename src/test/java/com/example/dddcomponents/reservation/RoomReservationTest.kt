package com.example.dddcomponents.reservation

import com.example.dddcomponents.reservation.RoomReservations.Reservation.OccurrencePolicy
import com.example.dddcomponents.user.Actor
import com.example.dddcomponents.user.ActorType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class RoomReservationTest {
    @Test
    fun givenRoomReservationWhenReservationCreatedAndAcceptedAndCanceledThenCorrectEventsShouldBeSaved() {
        val reservation = RoomReservations.createRoom("A102")

        val user = Actor(UUID.randomUUID(), ActorType.USER)
        val admin = Actor(UUID.randomUUID(), ActorType.ADMIN)
        val timeRange = TimeRange.createTimeRange(Instant.ofEpochSecond(1000), Instant.ofEpochSecond(2000))

        // TODO: Should I have here a method handle and pass a command?
        // Command from ApplicationService would also need na RoomReservations id to fetch from repository
        // Would ApplicationService use structure like:
        // new RequestReservationCommand(someId, new RequestReservation(...data...))
        val reservationId = reservation.requestReservation(
            RequestReservationDto(
                user,
                timeRange,
                OccurrencePolicy.OneTimeOccurrence
            )
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
                ReservationRequestCreated(reservationId, timeRange, OccurrencePolicy.OneTimeOccurrence),
                ReservationRequestAccepted(reservationId),
                RequestedToNotifyAdminsAboutCanceledReservation(reservationId, "A102"),
                ReservationCancelled(reservationId)
            ),
            reservation.domainEvents
        )
    }
}