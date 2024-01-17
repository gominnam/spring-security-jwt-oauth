package com.example.springsecurityjwtoauth.controller

import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/view")
class ViewController {
    @GetMapping("/service")
    fun redirect(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication.isAuthenticated) "/service"
        else "/index"
    }

    @GetMapping("/home")
    fun home(): String = "/index"

    @GetMapping("/oauth2")
    fun oauth2(): String = "/oauth2"
}