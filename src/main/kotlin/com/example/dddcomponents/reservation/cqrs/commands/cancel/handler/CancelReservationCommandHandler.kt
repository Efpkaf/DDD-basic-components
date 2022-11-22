package com.example.dddcomponents.reservation.cqrs.commands.cancel.handler

import com.example.dddcomponents.reservation.domain.RoomReservationRepository
import com.example.dddcomponents.reservation.cqrs.commands.cancel.CancelReservationCommand
import com.trendyol.kediatr.CommandHandler

data class CancelReservationCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<CancelReservationCommand> {

    override fun handle(command: CancelReservationCommand) {
        val roomReservation = roomReservationRepository.findRoomReservationsAggregateByReservationsId(command.reservationId)

        roomReservation.cancelReservation(command.actor, command.reservationId)
    }
}
