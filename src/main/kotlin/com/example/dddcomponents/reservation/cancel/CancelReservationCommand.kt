package com.example.dddcomponents.reservation.cancel

import com.example.dddcomponents.user.Actor
import com.trendyol.kediatr.Command
import java.util.*

data class CancelReservationCommand(
    val reservationId: UUID,
    val actor: Actor
) : Command
