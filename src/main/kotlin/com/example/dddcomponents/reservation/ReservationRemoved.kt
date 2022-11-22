package com.example.dddcomponents.reservation

import com.example.dddcomponents.sharedKernel.DomainEvent
import java.util.*

data class ReservationRemoved(val fromRoomId: String, val reservationId: UUID) : DomainEvent
