package com.example.dddcomponents.reservation.cqrs.commands.room

import com.example.dddcomponents.reservation.domain.RoomReservationRepository
import com.example.dddcomponents.reservation.domain.RoomReservationsAggregate
import com.trendyol.kediatr.CommandHandler
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
open class CreateRoomCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<CreateRoomCommand> {
    @Transactional
    override fun handle(command: CreateRoomCommand) {
        roomReservationRepository.save(RoomReservationsAggregate.createRoom(command.roomId))
    }
}
