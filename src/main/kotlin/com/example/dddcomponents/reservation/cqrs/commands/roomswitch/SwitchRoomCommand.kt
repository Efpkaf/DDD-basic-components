package com.example.dddcomponents.reservation.cqrs.commands.roomswitch

import com.trendyol.kediatr.Command
import java.util.*

data class SwitchRoomCommand(
    val fromRoom: String,
    val toRoom: String,
    val reservationId: UUID
) : Command
