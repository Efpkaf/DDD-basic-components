package com.example.dddcomponents.reservation.cqrs.queries.list

import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationCurrentStatus
import com.example.dddcomponents.reservation.cqrs.readmodel.ReservationStatusRepository
import com.trendyol.kediatr.QueryHandler
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
open class GetReservationsForTimeQueryHandler(
    val reservationStatusRepository: ReservationStatusRepository
) : QueryHandler<GetReservationsForTimeQuery, List<ReservationCurrentStatus>> {

    @Transactional
    override fun handle(query: GetReservationsForTimeQuery): List<ReservationCurrentStatus> {
        return reservationStatusRepository.findAll().toList()
    }
}
