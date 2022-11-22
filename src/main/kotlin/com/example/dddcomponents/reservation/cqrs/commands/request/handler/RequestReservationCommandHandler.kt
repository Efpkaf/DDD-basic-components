package com.example.dddcomponents.reservation.cqrs.commands.request.handler

import com.example.dddcomponents.reservation.domain.RoomReservationRepository
import com.example.dddcomponents.reservation.cqrs.commands.request.RequestReservationCommand
import com.trendyol.kediatr.CommandHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RequestReservationCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<RequestReservationCommand> {

    override fun handle(command: RequestReservationCommand) {
        val room = roomReservationRepository.findByIdOrNull(command.roomId)
        room?.requestReservation(command.actor, command.timeRange, command.occurrencePolicy)
    }
}
