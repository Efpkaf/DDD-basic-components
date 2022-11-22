package com.example.dddcomponents.reservation.cancel.handler

import com.example.dddcomponents.reservation.RoomReservationRepository
import com.example.dddcomponents.reservation.cancel.CancelReservationCommand
import com.trendyol.kediatr.CommandHandler

data class CancelReservationCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<CancelReservationCommand> {

    override fun handle(command: CancelReservationCommand) {
        val roomReservation = roomReservationRepository.findRoomByReservation(command.reservationId)

        roomReservation.cancelReservation(command.actor, command.reservationId)
    }
}
