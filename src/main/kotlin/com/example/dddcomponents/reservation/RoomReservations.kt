package com.example.dddcomponents.reservation

import com.example.dddcomponents.request.TimeRange
import com.example.dddcomponents.sharedKernel.Aggregate
import com.example.dddcomponents.sharedKernel.AggregateRoot
import com.example.dddcomponents.sharedKernel.DomainEvent
import com.example.dddcomponents.sharedKernel.Entity
import com.example.dddcomponents.user.Actor
import com.example.dddcomponents.user.ActorType
import java.util.*

//TODO: Should we add Aggregate and Entity to names or just annotate?
@Aggregate
class RoomReservations(val roomId: String) : AggregateRoot<String>(roomId) {
    private val reservations: MutableList<Reservation> = LinkedList()

    fun applyReservation(currentActor: Actor, possibleReservation: Reservation): List<DomainEvent> {
        val events: MutableList<DomainEvent> = LinkedList()

        assertOnlyAdminMayAcceptReservations(currentActor)
        assertReservationDoesNotOverlapExistingReservations(possibleReservation)

        events.addAll(possibleReservation.addToReservations(id, reservations))
        return events
    }

    private fun assertReservationDoesNotOverlapExistingReservations(newReservation: Reservation) {
        if (reservations.stream().anyMatch { it.overlaps(newReservation) }) {
            throw AlreadyExistingReservationForThisTimeException()
        }
    }

    private fun assertOnlyAdminMayAcceptReservations(currentActor: Actor) {
        if (currentActor.actorType != ActorType.ADMIN) {
            throw AcceptanceForbiddenForNotAdmin(currentActor.actorId)
        }
    }

    fun cancelReservation(currentActor: Actor, reservationId: UUID): List<DomainEvent> {
        val reservation = reservations.stream().filter { it.id == reservationId }.findFirst()
            .orElseThrow { -> NoSuchReservation(reservationId) }

        assertActorMayCancelReservation(currentActor, reservation)

        reservations.remove(reservation)

        val event: DomainEvent = when (currentActor.actorType) {
            ActorType.ADMIN -> ReservationCancelledByAdmin(reservation.id, roomId, reservation.owner.actorId)
            ActorType.USER -> ReservationCancelledByOwner(reservation.id, roomId, reservation.owner.actorId)
        }

        return listOf(event)
    }

    private fun assertActorMayCancelReservation(currentActor: Actor, reservation: Reservation) {
        when (currentActor.actorType) {
            ActorType.ADMIN -> {}
            ActorType.USER -> {
                if (reservation.owner.actorId != currentActor.actorId)
                    throw RejectionForbiddenForNotOwner(
                        reservation.id,
                        currentActor.actorId
                    )
            }
        }
    }


    class Reservation(
        override val id: UUID,
        val owner: Actor,
        val occurrencePolicy: OccurrencePolicy
    ) : Entity<UUID> {
        fun overlaps(reservation: Reservation): Boolean {
            return this.occurrencePolicy.overlaps(reservation)
        }

        fun addToReservations(roomId: String, list: MutableList<Reservation>): List<DomainEvent> {
            list.add(this)
            return occurrencePolicy.onReservationAccepted(roomId)
        }

        sealed interface OccurrencePolicy {
            fun overlaps(reservation: Reservation): Boolean
            fun onReservationAccepted(roomId: String): List<DomainEvent>

            class OneTimeOccurence(val timeRange: TimeRange) : OccurrencePolicy {
                override fun overlaps(reservation: Reservation): Boolean {
                    return when (val occurrencePolicy = reservation.occurrencePolicy) {
                        is OneTimeOccurence ->
                            occurrencePolicy.timeRange.timeTo > timeRange.timeFrom && occurrencePolicy.timeRange.timeFrom < timeRange.timeTo
                    }
                }

                override fun onReservationAccepted(roomId: String): List<DomainEvent> =
                    listOf(RoomHasBeenReserved(roomId, timeRange))
            }
        }

        companion object {
            fun createReservation(actor: Actor, timeRange: TimeRange): Reservation {
                return Reservation(UUID.randomUUID(), actor, OccurrencePolicy.OneTimeOccurence(timeRange))
            }
        }
    }
}

// TODO: Should failure be signalled by Exception or event?

class ReservationCancelledByAdmin(
    val reservationId: UUID,
    val roomId: String,
    val ownerId: UUID
) : DomainEvent {}

class ReservationCancelledByOwner(
    val reservationId: UUID,
    val roomId: String,
    val ownerId: UUID
) : DomainEvent {}

class AcceptanceForbiddenForNotAdmin(val actorId: UUID) : Exception()
class RejectionForbiddenForNotOwner(val reservationId: UUID, val actorId: UUID) : Exception()
class AlreadyExistingReservationForThisTimeException() : Exception() {}
class NoSuchReservation(val reservationId: UUID) : Exception() {}

// TODO: UUID should be some GenericId