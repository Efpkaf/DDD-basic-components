package com.example.dddcomponents.reservation

import com.example.dddcomponents.reservation.RoomReservationsAggregate.ReservationEntity.OccurrencePolicy
import com.example.dddcomponents.user.Actor
import com.example.dddcomponents.user.ActorType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.event.EventListener
import java.time.Instant
import java.util.*
import javax.transaction.Transactional

@SpringBootTest
open class RoomReservationsRepositoryTest {
    @Autowired
    lateinit var roomReservationRepository: RoomReservationRepository

    @Test
    @Transactional
    open fun givenSavedAggregateWhenCancelledReservationThenEventEmitted() {
        val roomReservations = RoomReservationsAggregate.createRoom("102A")
        val user = Actor(UUID.randomUUID(), ActorType.USER)
        val timeRange = TimeRange.createTimeRange(Instant.ofEpochSecond(1000), Instant.ofEpochSecond(2000))

        val id = roomReservations.requestReservation(
            user,
            timeRange,
            OccurrencePolicy.OneTimeOccurrence()
        )

        roomReservationRepository.save(roomReservations)


        val roomReservationsFetched = roomReservationRepository.findById("102A").get()
        roomReservationsFetched.cancelReservation(user, id)


        Assertions.assertEquals(
            roomReservationsFetched.domainEvents, listOf(
                ReservationCancelled(id)
            )
        )
    }
}