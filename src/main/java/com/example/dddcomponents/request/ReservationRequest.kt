package com.example.dddcomponents.request

import java.util.*
import java.util.function.Function

class ReservationRequest {
    lateinit var timeRange: TimeRange
    lateinit var roomNumber: Number
    lateinit var state: ReservationRequestState

    fun applyReservationRequest(callback: Function<ReservationRequestDTO, ReservationRequestState>) {
        val newState = callback.apply(
            ReservationRequestDTO(roomNumber, timeRange)
        )
        this.state = newState
    }

    companion object {
        fun createReservationRequest(
            roomNumber: Number,
            timeRange: TimeRange
        ): ReservationRequest {
            val reservationRequest = ReservationRequest()
            reservationRequest.state = ReservationRequestState.NEW
            reservationRequest.timeRange = timeRange
            reservationRequest.roomNumber = roomNumber
            return reservationRequest
        }
    }
}

data class ReservationRequestDTO(val roomNumber: Number, val timeRange: TimeRange);
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