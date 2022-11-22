package com.example.dddcomponents.reservation.accept.handler

import com.example.dddcomponents.reservation.RoomReservationRepository
import com.example.dddcomponents.reservation.accept.AcceptReservationCommand
import com.trendyol.kediatr.CommandHandler

data class AcceptReservationCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<AcceptReservationCommand> {

    override fun handle(command: AcceptReservationCommand) {
        val roomReservation = roomReservationRepository.findRoomByReservation(command.reservationId)

        roomReservation.acceptReservation(command.actor, command.reservationId)
    }
}
