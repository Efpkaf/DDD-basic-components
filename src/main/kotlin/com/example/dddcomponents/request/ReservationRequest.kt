package com.example.dddcomponents.request

import com.example.dddcomponents.sharedKernel.DomainEvent
import com.example.dddcomponents.user.Actor
import com.example.dddcomponents.user.ActorType
import java.util.*
import java.util.Collections.emptyList
import java.util.function.Consumer


class ReservationRequest {
    lateinit var id: UUID

    lateinit var timeRange: TimeRange
    lateinit var roomNumber: String

    lateinit var actor: Actor
    lateinit var state: ReservationRequestState

    fun reject(actor: Actor): List<DomainEvent> {
        assertActorMayRejectReservationRequest(actor)

        state = ReservationRequestState.REJECTED
        return listOf(ReservationRequestRejected(id))
    }

    fun assertActorMayRejectReservationRequest(actor: Actor) {
        when (actor.actorType) {
            ActorType.ADMIN -> {}
            ActorType.USER -> {
                if (actor.actorId != this.actor.actorId)
                    throw ThisActorMayNotRejectReservationRequest()
            }
        }
    }

    fun acceptReservationRequest(actor: Actor, callback: Consumer<ReservationRequestDTO>): List<DomainEvent> {
        assertActorMayApplyReservationRequest(actor)
        assertReservationRequestIsntAlreadyAcceptedOrRejected()

        callback.accept(
            ReservationRequestDTO(actor, roomNumber, timeRange)
        )

        this.state = ReservationRequestState.ACCEPTED

        return listOf(ReservationRequestAccepted(id))
    }

    private fun assertReservationRequestIsntAlreadyAcceptedOrRejected() {
        if (state != ReservationRequestState.NEW) {
            throw ReservationAlreadyRejectedOrAccepted()
        }
    }

    private fun assertActorMayApplyReservationRequest(actor: Actor) {
        if (actor.actorType != ActorType.ADMIN)
            throw ThisActorMayNotAcceptReservationRequest()
    }

    companion object {
        fun createReservationRequest(
            actor: Actor,
            roomNumber: String,
            timeRange: TimeRange
        ): Pair<ReservationRequest, List<DomainEvent>> {
            val reservationRequest = ReservationRequest()

            reservationRequest.id = UUID.randomUUID()
            reservationRequest.state = ReservationRequestState.NEW

            reservationRequest.actor = actor

            reservationRequest.timeRange = timeRange
            reservationRequest.roomNumber = roomNumber

            return Pair(reservationRequest, listOf(ReservationRequestCreated(reservationRequest.id)))
        }
    }
}

data class ReservationRequestDTO(val actor: Actor, val roomNumber: String, val timeRange: TimeRange);
data class TimeRange private constructor(val timeFrom: Date, val timeTo: Date) {

    companion object {
        fun createTimeRange(timeFrom: Date, timeTo: Date): TimeRange {
            if (!timeTo.after(timeFrom)) {
                throw IllegalArgumentException("Range must be positive")
            }
            return TimeRange(timeFrom, timeTo)
        }
    }
}


enum class ReservationRequestState {
    NEW, ACCEPTED, REJECTED
}

class ReservationRequestCreated(id: UUID) : DomainEvent
class ReservationRequestAccepted(id: UUID) : DomainEvent
class ReservationRequestRejected(id: UUID) : DomainEvent

class ThisActorMayNotRejectReservationRequest : Exception()
class ThisActorMayNotAcceptReservationRequest : Exception()
class ReservationAlreadyRejectedOrAccepted : Exception()
