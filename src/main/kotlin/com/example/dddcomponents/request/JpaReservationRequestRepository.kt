package com.example.dddcomponents.request

import org.springframework.data.repository.Repository
import java.util.*

@org.springframework.stereotype.Repository
interface JpaReservationRequestRepository : Repository<ReservationRequest, UUID>, ReservationRequestRepository {}