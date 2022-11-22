package com.example.dddcomponents.reservation.readmodel

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationStatusRepository: CrudRepository<ReservationCurrentStatus, String>
