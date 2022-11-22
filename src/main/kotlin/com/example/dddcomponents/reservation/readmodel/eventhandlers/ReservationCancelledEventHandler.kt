package com.example.dddcomponents.reservation.readmodel.eventhandlers

import com.example.dddcomponents.reservation.ReservationCancelled
import com.example.dddcomponents.reservation.readmodel.ReservationStatusRepository
import com.example.dddcomponents.sharedKernel.EventHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.event.TransactionalEventListener

@EventHandler
data class ReservationCancelledEventHandler(
    val reservationStatusRepository: ReservationStatusRepository
) {

    @TransactionalEventListener
    fun handle(event: ReservationCancelled) {
        val reservation = reservationStatusRepository.findByIdOrNull(event.id.toString())
        reservation?.cancel()
    }
}
