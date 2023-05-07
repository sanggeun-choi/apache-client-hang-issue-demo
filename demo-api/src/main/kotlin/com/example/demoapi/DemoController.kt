package com.example.demoapi

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RestController
class DemoController(
    private val externalApiClient: ExternalApiClient
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/api/demo/multiple")
    fun demo() {
        val threadSize = 50
        val service = Executors.newFixedThreadPool(threadSize)

        (0 until threadSize).forEach {
            service.execute {
                val result = externalApiClient.getMessages(ExternalApiClient.Req(UUID.randomUUID().toString()))
                log.info("##result : $result")
            }
        }

        service.shutdown()
        service.awaitTermination(1, TimeUnit.HOURS)
    }

    @GetMapping("/api/demo/single")
    fun demo2() {
        val result = externalApiClient.getMessages(ExternalApiClient.Req(UUID.randomUUID().toString()))
        log.info("##result : $result")
    }
}