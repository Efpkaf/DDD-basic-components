package com.example.dddcomponents

import com.example.dddcomponents.reservation.ReservationRequestCreated
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.event.EventListener


@SpringBootApplication
open class DddComponentsApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DddComponentsApplication::class.java, *args)
        }
    }
}