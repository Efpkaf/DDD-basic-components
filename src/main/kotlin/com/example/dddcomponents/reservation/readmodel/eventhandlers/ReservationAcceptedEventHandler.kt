package com.example.dddcomponents.reservation.readmodel.eventhandlers

import com.example.dddcomponents.reservation.ReservationRequestAccepted
import com.example.dddcomponents.reservation.readmodel.ReservationStatusRepository
import com.example.dddcomponents.sharedKernel.EventHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.event.TransactionalEventListener

@EventHandler
data class ReservationAcceptedEventHandler(
    val reservationStatusRepository: ReservationStatusRepository
) {
// [ ] commandy któe tworzą rezerwacje (bez controllerow, command handler)
    @TransactionalEventListener
    fun handle(event: ReservationRequestAccepted) {
        val reservation = reservationStatusRepository.findByIdOrNull(event.id.toString())
        reservation?.accept()
    }
}
