package com.example.springsecurityjwtoauth.controller

import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ViewController {
    @ResponseStatus(OK)
    @GetMapping("/home")
    fun greet(): String = "Welcome to secured endpoint!"

}