package com.example.dddcomponents.reservation.domain

import java.time.Instant
import javax.persistence.Embeddable

@Embeddable
data class TimeRange private constructor(val timeFrom: Instant, val timeTo: Instant) {
    companion object {
        fun createTimeRange(timeFrom: Instant, timeTo: Instant): TimeRange {
            if (!timeTo.isAfter(timeFrom)) {
                throw IllegalArgumentException("Range must be positive")
            }
            return TimeRange(timeFrom, timeTo)
        }
    }
}