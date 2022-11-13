package com.example.dddcomponents.reservation

import com.example.dddcomponents.request.TimeRange
import com.example.dddcomponents.sharedKernel.Aggregate
import com.example.dddcomponents.sharedKernel.AggregateRoot
import com.example.dddcomponents.sharedKernel.DomainEvent
import com.example.dddcomponents.sharedKernel.Entity
import java.util.*

//TODO: Should we add Aggregate and Entity to names or just annotate?
@Aggregate
class RoomReservations(roomId: String) : AggregateRoot<String>(roomId) {
    private val reservations: MutableList<Reservation> = Collections.emptyList()

    fun applyReservation(possibleReservation: Reservation): List<DomainEvent> {
        val events: MutableList<DomainEvent> = LinkedList()

        assertReservationDoesNotOverlapExistingReservations(possibleReservation)

        events.addAll(possibleReservation.addToReservations(id, reservations))
        return events
    }

    // TODO: Reservation should also have userId so we know who to notify
    fun cancelReservation(reservationId: String): List<ReservationCancelled> {
        val reservation = reservations.stream().filter { it.id.equals(reservationId) }.findFirst()

        if (reservation.isEmpty) {
            throw IllegalArgumentException("No such reservation")
        }

        reservations.remove(reservation.get())
        return Arrays.asList(
            ReservationCancelled(reservation.get().id)
        )
    }

    private fun assertReservationDoesNotOverlapExistingReservations(newReservation: Reservation) {
        if (reservations.stream().anyMatch { it.overlaps(newReservation) }) {
            throw TimeSlotOverlaps()
        }
    }

    class Reservation(
        override val id: UUID,
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
                    Arrays.asList(RoomHasBeenReserved(roomId, timeRange))
            }
        }

        companion object {
            fun createReservation(timeRange: TimeRange): Reservation {
                return Reservation(UUID.randomUUID(), OccurrencePolicy.OneTimeOccurence(timeRange))
            }
        }
    }

    class Actor
}

// TODO: Should failure be signalled by Exception or event?
class TimeSlotOverlaps() : Exception() {}
// TODO: lShould we put all exceptions into the class?

class ReservationCancelled(id: UUID) : DomainEvent {}