package com.example.dddcomponents.reservation

import org.springframework.data.repository.CrudRepository

interface RoomReservationRepository : CrudRepository<RoomReservationsAggregate, String>
