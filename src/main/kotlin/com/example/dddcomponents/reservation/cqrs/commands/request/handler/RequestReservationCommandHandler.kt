package com.example.dddcomponents.reservation.cqrs.commands.request.handler

import com.example.dddcomponents.reservation.domain.RoomReservationRepository
import com.example.dddcomponents.reservation.cqrs.commands.request.RequestReservationCommand
import com.trendyol.kediatr.CommandHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
open class RequestReservationCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<RequestReservationCommand> {

    @Transactional
    override fun handle(command: RequestReservationCommand) {
        val room = roomReservationRepository.findByIdOrNull(command.roomId)
        room?.let {
            it.requestReservation(command.actor, command.timeRange, command.occurrencePolicy)
            roomReservationRepository.save(it)
        }
    }
}
