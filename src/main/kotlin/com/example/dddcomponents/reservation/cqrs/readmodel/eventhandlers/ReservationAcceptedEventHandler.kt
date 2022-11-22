package com.example.dddcomponents.reservation.cqrs.readmodel.eventhandlers

import com.example.dddcomponents.reservation.domain.ReservationRequestAccepted
import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationStatusRepository
import com.example.dddcomponents.sharedKernel.EventHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@EventHandler
data class ReservationAcceptedEventHandler(
    val reservationStatusRepository: ReservationStatusRepository
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: ReservationRequestAccepted) {
        val reservation = reservationStatusRepository.findByIdOrNull(event.id)
        reservation?.accept()
    }
}
