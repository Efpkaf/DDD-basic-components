package com.example.dddcomponents.request

import org.springframework.data.repository.Repository
import java.util.*


interface JpaReservationRequestRepository : Repository<ReservationRequest, UUID>, ReservationRequestRepository {}