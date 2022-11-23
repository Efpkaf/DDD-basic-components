package com.example.dddcomponents.reservation

import com.example.dddcomponents.reservation.cqrs.commands.accept.AcceptReservationCommand
import com.example.dddcomponents.reservation.cqrs.commands.accept.handler.AcceptReservationCommandHandler
import com.example.dddcomponents.reservation.cqrs.commands.cancel.CancelReservationCommand
import com.example.dddcomponents.reservation.cqrs.commands.cancel.handler.CancelReservationCommandHandler
import com.example.dddcomponents.reservation.cqrs.commands.request.RequestReservationCommand
import com.example.dddcomponents.reservation.cqrs.commands.request.handler.RequestReservationCommandHandler
import com.example.dddcomponents.reservation.cqrs.commands.room.CreateRoomCommand
import com.example.dddcomponents.reservation.cqrs.commands.room.CreateRoomCommandHandler
import com.example.dddcomponents.reservation.cqrs.queries.list.GetReservationsForTimeQuery
import com.example.dddcomponents.reservation.cqrs.queries.list.GetReservationsForTimeQueryHandler
import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationStatus
import com.example.dddcomponents.reservation.domain.RoomReservationsAggregate.ReservationEntity.OccurrencePolicy
import com.example.dddcomponents.reservation.domain.TimeRange
import com.example.dddcomponents.user.Actor
import com.example.dddcomponents.user.ActorType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest
open class RoomReservationAcceptanceTest {
    @Autowired
    lateinit var createRoomCommandHandler: CreateRoomCommandHandler;

    @Autowired
    lateinit var requestReservationCommandHandler: RequestReservationCommandHandler

    @Autowired
    lateinit var getReservationsForTimeQueryHandler: GetReservationsForTimeQueryHandler

    @Autowired
    lateinit var acceptReservationCommandHandler: AcceptReservationCommandHandler

    @Autowired
    lateinit var cancelReservationCommandHandler: CancelReservationCommandHandler

    @Test
    open fun testProcess() {
        val roomId = "A102"
        val user = Actor(UUID.randomUUID(), ActorType.USER)
        val admin = Actor(UUID.randomUUID(), ActorType.ADMIN)
        val timeRange = TimeRange.createTimeRange(Instant.ofEpochSecond(999), Instant.ofEpochSecond(2000))

        createRoomCommandHandler.handle(CreateRoomCommand(roomId))

        requestReservationCommandHandler.handle(
            RequestReservationCommand(
                roomId,
                user,
                timeRange,
                OccurrencePolicy.OneTimeOccurrence()
            )
        )

        val results = getReservationsForTimeQueryHandler.handle(
            GetReservationsForTimeQuery(
                roomId,
                TimeRange.createTimeRange(
                    Instant.ofEpochSecond(0),
                    Instant.ofEpochSecond(4000)
                ),
                listOf(ReservationStatus.PENDING)
            )
        )

        val reservationCurrentStatus = results.first()

        acceptReservationCommandHandler.handle(
            AcceptReservationCommand(
                reservationCurrentStatus.id,
                admin
            )
        )

        cancelReservationCommandHandler.handle(CancelReservationCommand(
            reservationCurrentStatus.id,
            user
        ))

        val newResults = getReservationsForTimeQueryHandler.handle(
            GetReservationsForTimeQuery(
                roomId,
                TimeRange.createTimeRange(
                    Instant.ofEpochSecond(0),
                    Instant.ofEpochSecond(4000)
                ),
                listOf(ReservationStatus.PENDING)
            )
        )

        Assertions.assertEquals(
            newResults.first(),
            reservationCurrentStatus.copy(status = ReservationStatus.CANCELLED)
        )
    }
}
