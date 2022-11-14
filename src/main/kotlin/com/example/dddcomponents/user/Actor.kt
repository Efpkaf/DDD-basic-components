package com.example.dddcomponents.user

import java.util.*

data class Actor(val actorId: UUID, val actorType: ActorType) {
}

enum class ActorType {
    ADMIN, USER
}