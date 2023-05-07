package com.example.demoapi

import feign.Client
import feign.Response
import feign.httpclient.ApacheHttpClient
import org.apache.http.impl.client.HttpClients
import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import java.nio.charset.StandardCharsets.UTF_8

@FeignClient(name = "external-client", url = "\${external.api.url}", configuration = [ExternalApiClient.Config::class])
interface ExternalApiClient {
    @GetMapping("/api/external/messages")
    fun getMessages(@RequestBody req: Req): String

    data class Req(
        val id: String
    )

    class Config {
        @Bean
        fun client(): Client {
            val log = LoggerFactory.getLogger(this::class.java)

            val httpClient = HttpClients.custom()
                .setMaxConnPerRoute(10)
                .setMaxConnTotal(10)
                .build()

            val client = ApacheHttpClient(httpClient)

            return Client { request, options ->
                val response = client.execute(request, options)

//                val body = response.use { it.body().asInputStream().bufferedReader().readText() } // correct
                val body = response.body()?.asInputStream()?.bufferedReader()?.readText() // incorrect

                log.info("## body : $body, response : ${response.headers()}")

                Response.builder()
                    .request(request)
                    .body(body, UTF_8)
                    .headers(mapOf("content-type" to listOf("application/json")))
                    .status(200)
                    .build()
            }
        }
    }
}