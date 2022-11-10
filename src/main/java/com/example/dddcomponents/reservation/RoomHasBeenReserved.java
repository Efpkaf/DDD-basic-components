package com.example.dddcomponents.reservation;

import com.example.dddcomponents.request.TimeRange;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class RoomHasBeenReserved {
    private final String room;
    private final TimeRange range;
}
