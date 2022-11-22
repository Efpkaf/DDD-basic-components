package com.example.dddcomponents.reservation.cqrs.commands.room

import com.example.dddcomponents.reservation.domain.RoomReservationRepository
import com.example.dddcomponents.reservation.domain.RoomReservationsAggregate
import com.trendyol.kediatr.CommandHandler

class CreateRoomCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<CreateRoomCommand> {

    override fun handle(command: CreateRoomCommand) {
        roomReservationRepository.save(RoomReservationsAggregate.createRoom(command.roomId))
    }
}
