package com.example.dddcomponents.reservation.readmodel.eventhandlers

import com.example.dddcomponents.reservation.ReservationRequestCreated
import com.example.dddcomponents.reservation.readmodel.ReservationCurrentStatus
import com.example.dddcomponents.reservation.readmodel.ReservationStatus
import com.example.dddcomponents.reservation.readmodel.ReservationStatusRepository
import com.example.dddcomponents.sharedKernel.EventHandler
import org.springframework.transaction.event.TransactionalEventListener

@EventHandler
data class ReservationRequestCreatedEventHandler(
    val reservationStatusRepository: ReservationStatusRepository
) {

    @TransactionalEventListener
    fun handle(event: ReservationRequestCreated) {
        reservationStatusRepository.save(ReservationCurrentStatus(event.id.toString(), ReservationStatus.PENDING, event.timeRange.timeFrom, event.timeRange.timeTo))
    }
}
