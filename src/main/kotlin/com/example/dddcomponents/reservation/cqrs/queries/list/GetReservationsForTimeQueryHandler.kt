package com.example.dddcomponents.reservation.cqrs.queries.list

import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationCurrentStatus
import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationStatusRepository
import com.trendyol.kediatr.QueryHandler
import org.springframework.stereotype.Service

@Service
data class GetReservationsForTimeQueryHandler(
    val reservationStatusRepository: ReservationStatusRepository
) : QueryHandler<GetReservationsForTimeQuery, List<ReservationCurrentStatus>> {

    override fun handle(query: GetReservationsForTimeQuery): List<ReservationCurrentStatus> {
        return reservationStatusRepository.findForTimeRange(
            query.roomId,
            query.status,
            query.range.timeFrom,
            query.range.timeTo
        );
    }
}
