package com.example.dddcomponents.reservation

import com.example.dddcomponents.request.ReservationRequest
import com.example.dddcomponents.request.TimeRange
import com.example.dddcomponents.sharedKernel.Aggregate
import com.example.dddcomponents.sharedKernel.AggregateRoot
import java.util.*

@Aggregate
class RoomReservations(id: String?) : AggregateRoot<String>() {
    private lateinit var reservations: List<TimeRange>

    //newReservationFrom newReservationTo
    fun acceptReservationRequest(reservationRequest: ReservationRequest): RoomHasBeenReserved {
        for ((existingReservationsDateFrom, existingReservationDateTo) in reservations) {
            val newReservationsDateFrom = reservationRequest.timeRange.timeFrom
            val newReservationsDateTo = reservationRequest.timeRange.timeTo
            if (isOverlapping(newReservationsDateTo, existingReservationsDateFrom)) { //TODO fix dates
                throw AlreadyExistingReservationForThisDateException()
            }
        }
        reservations.add(reservationRequest.timeRange)
        return RoomHasBeenReserved(id, reservationRequest.timeRange)
    }

    companion object {
        private fun isOverlapping(newReservationsDateTo: Date, existingReservationsDateFrom: Date): Boolean {
            return newReservationsDateTo.before(existingReservationsDateFrom) && true
        }
    }
}
