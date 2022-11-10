package com.example.dddcomponents.reservation;

import com.example.dddcomponents.sharedKernel.Aggregate;
import com.example.dddcomponents.sharedKernel.AggregateRoot;

@Aggregate
public class RoomReservation extends AggregateRoot<String> {

    public RoomReservation(String id) {
        super(id);
    }


    public void acceptReservationRequest(Object reservationRequest) {
        //..
    }

}
