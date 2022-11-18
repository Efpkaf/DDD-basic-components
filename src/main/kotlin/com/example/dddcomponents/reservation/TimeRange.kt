package com.example.dddcomponents.reservation

import java.time.Instant

// TODO: UUID should be some GenericId
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