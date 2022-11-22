package com.example.dddcomponents.reservation.cqrs.readmodel.eventhandlers

import com.example.dddcomponents.reservation.domain.ReservationRequestCreated
import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationCurrentStatus
import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationStatus
import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationStatusRepository
import com.example.dddcomponents.sharedKernel.EventHandler
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@EventHandler
data class ReservationRequestCreatedEventHandler(
    val reservationStatusRepository: ReservationStatusRepository
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: ReservationRequestCreated) {
        reservationStatusRepository.save(
            ReservationCurrentStatus(
                event.id,
                event.roomId,
                ReservationStatus.PENDING,
                event.timeRange.timeFrom,
                event.timeRange.timeTo
            )
        )
    }
}
