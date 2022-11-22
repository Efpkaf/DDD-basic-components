package com.example.dddcomponents.reservation.cqrs.commands.accept.handler

import com.example.dddcomponents.reservation.domain.RoomReservationRepository
import com.example.dddcomponents.reservation.cqrs.commands.accept.AcceptReservationCommand
import com.trendyol.kediatr.CommandHandler
import org.springframework.stereotype.Service

@Service
data class AcceptReservationCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<AcceptReservationCommand> {

    override fun handle(command: AcceptReservationCommand) {
        val roomReservation = roomReservationRepository.findRoomByReservations(command.reservationId)

        roomReservation.acceptReservation(command.actor, command.reservationId)
    }
}
