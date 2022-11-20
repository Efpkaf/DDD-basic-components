package com.example.dddcomponents.user

import java.util.*
import javax.persistence.Embeddable

@Embeddable
data class Actor(val actorId: UUID, val actorType: ActorType) {
}

enum class ActorType {
    ADMIN, USER
}