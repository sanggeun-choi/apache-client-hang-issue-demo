package com.example.externalapi

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController {
    @GetMapping("/api/messages")
    fun getMessages(): String {
        return "Hello World"
    }
}