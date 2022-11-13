package com.example.dddcomponents.reservation

import com.example.dddcomponents.request.TimeRange
import com.example.dddcomponents.sharedKernel.DomainEvent

data class RoomHasBeenReserved(val room: String, val range: TimeRange) : DomainEvent {}
