package com.example.dddcomponents.reservation

import com.example.dddcomponents.reservation.cqrs.commands.accept.handler.AcceptReservationCommandHandler
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
import javax.transaction.Transactional

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


    @Test
    open fun givenRoomReservationWhenReservationCreatedAndAcceptedAndCanceledThenCorrectEventsShouldBeSaved() {
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

        Assertions.assertEquals(0, results.size)

//        acceptReservationCommandHandler.handle(AcceptReservationCommand())


//
//        val reservationId = reservation.requestReservation(
//            user,
//            timeRange,
//            OccurrencePolicy.OneTimeOccurrence()
//        )
//
//        reservation.acceptReservation(
//            admin,
//            reservationId
//        )
//
//        reservation.cancelReservation(
//            user,
//            reservationId
//        )
//
//        Assertions.assertEquals(
//            listOf(
//                ReservationRequestCreated(reservationId, timeRange, OccurrencePolicy.OneTimeOccurrence()),
//                ReservationRequestAccepted(reservationId),
//                RequestedToNotifyAdminsAboutCanceledReservation(reservationId, "A102"),
//                ReservationCancelled(reservationId)
//            ),
//            reservation.domainEvents
//        )
    }
}
