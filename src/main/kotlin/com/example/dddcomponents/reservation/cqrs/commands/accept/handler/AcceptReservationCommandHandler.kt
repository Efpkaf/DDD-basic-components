package com.example.dddcomponents.reservation.cqrs.commands.accept.handler

import com.example.dddcomponents.reservation.domain.RoomReservationRepository
import com.example.dddcomponents.reservation.cqrs.commands.accept.AcceptReservationCommand
import com.trendyol.kediatr.CommandHandler
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
open class AcceptReservationCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<AcceptReservationCommand> {

    @Transactional
    override fun handle(command: AcceptReservationCommand) {
        val roomReservation = roomReservationRepository.findRoomReservationsAggregateByReservationsId(command.reservationId)

        roomReservation.acceptReservation(command.actor, command.reservationId)
        roomReservationRepository.save(roomReservation)
    }
}
