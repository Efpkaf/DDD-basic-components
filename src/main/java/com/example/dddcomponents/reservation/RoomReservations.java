package com.example.dddcomponents.reservation;

import com.example.dddcomponents.request.ReservationRequest;
import com.example.dddcomponents.request.TimeRange;
import com.example.dddcomponents.sharedKernel.Aggregate;
import com.example.dddcomponents.sharedKernel.AggregateRoot;

import java.util.Date;
import java.util.List;

@Aggregate
public class RoomReservations extends AggregateRoot<String> {

    private List<TimeRange> reservations;

    public RoomReservations(String id) {
        super(id);
    }

//newReservationFrom newReservationTo
    public RoomHasBeenReserved acceptReservationRequest(ReservationRequest reservationRequest) {

        for (TimeRange reservation : reservations) {
            var newReservationsDateFrom = reservationRequest.getTimeRange().component1();
            var newReservationsDateTo = reservationRequest.getTimeRange().component2();

            var existingReservationsDateFrom = reservation.component1();
            var existingReservationDateTo = reservation.component2();

            if(isOverlapping(newReservationsDateTo, existingReservationsDateFrom)) {//TODO fix dates
                throw new AlreadyExistingReservationForThisDateException();
            }
        }

        reservations.add(reservationRequest.timeRange);
        ;
        return new RoomHasBeenReserved(id, reservationRequest.getTimeRange());
    }

    private static boolean isOverlapping(Date newReservationsDateTo, Date existingReservationsDateFrom) {
        return newReservationsDateTo.before(existingReservationsDateFrom) && true;
    }

}
