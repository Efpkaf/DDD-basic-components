package com.example.dddcomponents.reservation.cqrs.commands.accept

import com.example.dddcomponents.user.Actor
import com.trendyol.kediatr.Command
import java.util.*

data class AcceptReservationCommand(
    val reservationId: UUID,
    val actor: Actor
) : Command
