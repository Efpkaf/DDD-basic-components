package com.example.dddcomponents.room

import com.example.dddcomponents.reservation.RoomReservationRepository
import com.example.dddcomponents.reservation.RoomReservationsAggregate
import com.trendyol.kediatr.CommandHandler

class CreateRoomCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<CreateRoomCommand> {

    override fun handle(command: CreateRoomCommand) {
        roomReservationRepository.save(RoomReservationsAggregate.createRoom(command.roomId))
    }
}
