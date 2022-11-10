package com.example.dddcomponents.reservation

import com.example.dddcomponents.request.TimeRange
import lombok.RequiredArgsConstructor

data class RoomHasBeenReserved(val room: String, val range: TimeRange) {}
