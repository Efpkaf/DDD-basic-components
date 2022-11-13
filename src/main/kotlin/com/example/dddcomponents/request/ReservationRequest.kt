package com.example.dddcomponents.request

import com.example.dddcomponents.sharedKernel.DomainEvent
import java.util.*
import java.util.Collections.emptyList
import java.util.function.Consumer


class ReservationRequest {
    lateinit var id: UUID
    lateinit var timeRange: TimeRange
    lateinit var roomNumber: String
    lateinit var state: ReservationRequestState

    fun reject(): List<DomainEvent> {
        state = ReservationRequestState.REJECTED
        return emptyList()
    }

    fun applyReservationRequest(callback: Consumer<ReservationRequestDTO>): List<DomainEvent> {
        if (state != ReservationRequestState.NEW) {
            throw IllegalStateException("reservation already applied")
        }

        callback.accept(
            ReservationRequestDTO(roomNumber, timeRange)
        )

        this.state = ReservationRequestState.ACCEPTED
        return emptyList()
    }

    companion object {
        fun createReservationRequest(
            roomNumber: String,
            timeRange: TimeRange
        ): ReservationRequest {
            val reservationRequest = ReservationRequest()
            reservationRequest.id = UUID.randomUUID()
            reservationRequest.state = ReservationRequestState.NEW
            reservationRequest.timeRange = timeRange
            reservationRequest.roomNumber = roomNumber
            return reservationRequest
        }
    }
}

data class ReservationRequestDTO(val roomNumber: String, val timeRange: TimeRange);
data class TimeRange private constructor(val timeFrom: Date, val timeTo: Date) {

    companion object {
        fun createTimeRange(timeFrom: Date, timeTo: Date): TimeRange {
            if (!timeTo.after(timeFrom)) {
                throw IllegalArgumentException("Range must be positive")
            }
            return TimeRange(timeFrom, timeTo)
        }
    }
}


enum class ReservationRequestState {
    NEW, ACCEPTED, REJECTED
}