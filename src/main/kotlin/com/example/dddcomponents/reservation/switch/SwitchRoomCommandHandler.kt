package com.example.dddcomponents.reservation.switch

import com.example.dddcomponents.reservation.ReservationService
import com.example.dddcomponents.reservation.RoomReservationRepository
import com.trendyol.kediatr.CommandHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
data class SwitchRoomCommandHandler(
    private val roomReservationRepository: RoomReservationRepository
) : CommandHandler<SwitchRoomCommand> {
    override fun handle(command: SwitchRoomCommand) {
        val fromRoom = roomReservationRepository.findByIdOrNull(command.fromRoom)
        val toRoom = roomReservationRepository.findByIdOrNull(command.toRoom)

        ReservationService.changeRoom(fromRoom!!, toRoom!!, command.reservationId)
    }
}
