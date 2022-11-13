package com.example.dddcomponents.request

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest
class JpaReservationRequestRepositoryTest {
    @Autowired
    lateinit var reservationRequestRepository: JpaReservationRequestRepository

    @Test
    fun whenReservationIsSavedThenCanBeFetched() {
        val reservationRequest = ReservationRequest.createReservationRequest(
            "A102", TimeRange.createTimeRange(
                Date.from(Instant.ofEpochSecond(1000)),
                Date.from(Instant.ofEpochSecond(2000))
            )
        )

        reservationRequestRepository.save(reservationRequest)
        val fetchedReservationRequest = reservationRequestRepository.findById(reservationRequest.id)

        assertEquals(reservationRequest, fetchedReservationRequest)
    }
}