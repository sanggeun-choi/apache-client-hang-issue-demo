package com.example.externalapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExternalApiApplication

fun main(args: Array<String>) {
    runApplication<ExternalApiApplication>(*args)
}
