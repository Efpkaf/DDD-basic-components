package com.example.dddcomponents.reservation

import com.example.dddcomponents.sharedKernel.Aggregate
import com.example.dddcomponents.sharedKernel.AggregateRoot
import com.example.dddcomponents.sharedKernel.DomainEvent
import com.example.dddcomponents.sharedKernel.Entity
import com.example.dddcomponents.user.Actor
import com.example.dddcomponents.user.ActorType
import org.springframework.data.domain.DomainEvents
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.IntStream
import kotlin.streams.toList

//TODO: Should we add Aggregate and Entity to names or just annotate?
@Aggregate
class RoomReservations(val roomId: String) : AggregateRoot<String>(roomId) {
    @get:DomainEvents
    val domainEvents: MutableList<DomainEvent> = LinkedList()

    private val reservations: MutableList<Reservation> = LinkedList()

    fun requestReservation(reservationRequestDTO: RequestReservationDto): UUID {
        val reservation = Reservation(
            UUID.randomUUID(),
            reservationRequestDTO.actor,
            reservationRequestDTO.timeRange,
            reservationRequestDTO.occurencePolicy
        )

        reservations.add(
            reservation
        )

        domainEvents.add(
            ReservationRequestCreated(
                reservation.id,
                reservationRequestDTO.timeRange,
                reservationRequestDTO.occurencePolicy
            )
        )

        return reservation.id
    }

    fun acceptReservation(currentActor: Actor, reservationId: UUID) {
        val reservation = reservations.stream()
            .filter { x -> x.reservationState == ReservationState.PENDING }
            .filter { x -> x.id == reservationId }
            .findAny()
            .orElseThrow { NoSuchReservation(reservationId) }

        assertOnlyAdminMayAcceptReservations(currentActor)
        assertReservationDoesNotOverlapExistingReservations(reservation)

        reservation.reservationState = ReservationState.ACCEPTED

        domainEvents.add(ReservationRequestAccepted(reservationId))
    }


    private fun assertOnlyAdminMayAcceptReservations(currentActor: Actor) {
        if (currentActor.actorType != ActorType.ADMIN) {
            throw AcceptanceForbiddenForNotAdmin(currentActor.actorId)
        }
    }

    private fun assertReservationDoesNotOverlapExistingReservations(newReservation: Reservation) {
        if (reservations.stream()
                .filter { x -> x.reservationState == ReservationState.ACCEPTED }
                .anyMatch { it.overlaps(newReservation) }
        ) {
            throw AlreadyExistingReservationForThisTimeException()
        }
    }

    fun cancelReservation(currentActor: Actor, reservationId: UUID) {
        val reservation = reservations.stream()
            .filter { it.id == reservationId }.findFirst()
            .orElseThrow { -> NoSuchReservation(reservationId) }

        assertActorMayCancelReservation(currentActor, reservation)

        reservations.remove(reservation)

        handleRejectionNotifications(currentActor, reservation)
        domainEvents.add(ReservationCancelled(reservationId))
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

    private fun handleRejectionNotifications(currentActor: Actor, reservation: Reservation) {
        domainEvents.addAll(
            when (reservation.reservationState) {
                ReservationState.PENDING -> when (currentActor.actorType) {
                    ActorType.ADMIN -> listOf(
                        RequestedToNotifyUserAboutCanceledReservation(
                            reservation.id,
                            roomId,
                            reservation.owner.actorId
                        )
                    )

                    ActorType.USER -> emptyList()
                }

                ReservationState.ACCEPTED ->
                    when (currentActor.actorType) {
                        ActorType.ADMIN -> listOf(
                            RequestedToNotifyUserAboutCanceledReservation(
                                reservation.id,
                                roomId,
                                reservation.owner.actorId
                            )
                        )

                        ActorType.USER -> listOf(
                            RequestedToNotifyAdminsAboutCanceledReservation(reservation.id, roomId)
                        )
                    }
            }
        )
    }

    class Reservation(
        override val id: UUID,
        val owner: Actor,
        val timeRange: TimeRange,
        val occurrencePolicy: OccurrencePolicy,
        var reservationState: ReservationState = ReservationState.PENDING
    ) : Entity<UUID> {
        fun overlaps(reservation: Reservation): Boolean {
            val allThisRanges = occurrencePolicy.generateAllTimeRanges(timeRange)
            val allThatRanges = reservation.occurrencePolicy.generateAllTimeRanges(reservation.timeRange)

            for (thisRange in allThisRanges) {
                for (thatRange in allThatRanges) {
                    if (thisRange.timeTo > thatRange.timeFrom && thisRange.timeFrom < thatRange.timeTo) {
                        return true
                    }
                }
            }

            return false
        }

        sealed interface OccurrencePolicy {
            fun generateAllTimeRanges(initialTimeRange: TimeRange): List<TimeRange>

            object OneTimeOccurrence : OccurrencePolicy {
                override fun generateAllTimeRanges(initialTimeRange: TimeRange): List<TimeRange> {
                    return listOf(initialTimeRange)
                }
            }

            class WeeklyOccurrence(val until: Instant) : OccurrencePolicy {
                override fun generateAllTimeRanges(initialTimeRange: TimeRange): List<TimeRange> =
                    IntStream.iterate(0) { n -> n + 1 }
                        .mapToObj { advance ->
                            TimeRange.createTimeRange(
                                initialTimeRange.timeFrom.plus(advance.toLong(), ChronoUnit.WEEKS),
                                initialTimeRange.timeFrom.plus(advance.toLong(), ChronoUnit.WEEKS),
                            )
                        }
                        .takeWhile { timeRange -> timeRange.timeTo.isBefore(until) }
                        .toList()
            }
        }
    }

    companion object {
        fun createRoom(roomName: String) = RoomReservations(roomName)
    }
}


enum class ReservationState {
    PENDING, ACCEPTED
}

data class RequestedToNotifyUserAboutCanceledReservation(
    val reservationId: UUID,
    val roomId: String,
    val ownerId: UUID
) : DomainEvent

data class RequestedToNotifyAdminsAboutCanceledReservation(
    val reservationId: UUID,
    val roomId: String,
) : DomainEvent

data class ReservationRequestCreated(
    val id: UUID,
    val timeRange: TimeRange,
    val occurrencePolicy: RoomReservations.Reservation.OccurrencePolicy
) : DomainEvent

data class ReservationRequestAccepted(val id: UUID) : DomainEvent
data class ReservationCancelled(val id: UUID) : DomainEvent

class AcceptanceForbiddenForNotAdmin(val actorId: UUID) : Exception()
class RejectionForbiddenForNotOwner(val reservationId: UUID, val actorId: UUID) : Exception()
class AlreadyExistingReservationForThisTimeException() : Exception() {}
class NoSuchReservation(val reservationId: UUID) : Exception() {}

