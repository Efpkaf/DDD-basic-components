package com.example.dddcomponents.reservation.switch

import com.trendyol.kediatr.Command
import java.util.*

data class SwitchRoomCommand(
    val fromRoom: String,
    val toRoom: String,
    val reservationId: UUID
) : Command
