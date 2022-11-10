package com.example.dddcomponents.reservation;

import com.example.dddcomponents.sharedKernel.AggregateRoot;

public class Reservation extends AggregateRoot<String> {

    public Reservation(String id) {
        super(id);
    }


}
