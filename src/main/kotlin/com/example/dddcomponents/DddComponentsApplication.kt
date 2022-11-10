package com.example.dddcomponents

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
object DddComponentsApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(DddComponentsApplication::class.java, *args)
    }
}
