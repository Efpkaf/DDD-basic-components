package com.example.dddcomponents.reservation.status

import com.example.dddcomponents.reservation.readmodel.ReservationCurrentStatus
import com.example.dddcomponents.reservation.readmodel.ReservationStatusRepository
import com.trendyol.kediatr.QueryHandler

data class GetReservationsForTimeQueryHandler(
    val reservationStatusRepository: ReservationStatusRepository
) : QueryHandler<GetReservationsForTimeQuery, ReservationCurrentStatus> {

    override fun handle(query: GetReservationsForTimeQuery): ReservationCurrentStatus {
        return reservationStatusRepository.findAcceptedForTimeRange(
            query.roomId,
            query.range.timeFrom,
            query.range.timeTo
        );
    }
}
