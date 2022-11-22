package com.example.dddcomponents.reservation

import com.example.dddcomponents.sharedKernel.DomainEvent
import java.util.*

data class ReservationInserted(val toRoomId: String, val reservationId: UUID) : DomainEvent
