package com.example.springsecurityjwtoauth.controller

import com.example.springsecurityjwtoauth.model.authentication.AuthenticationRequest
import com.example.springsecurityjwtoauth.model.authentication.AuthenticationResponse
import com.example.springsecurityjwtoauth.model.authentication.RegisterRequest
import com.example.springsecurityjwtoauth.security.oauth2.user.CustomOAuth2UserService
import com.example.springsecurityjwtoauth.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI


@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val userService: UserService,
) {
    @ResponseStatus(CREATED)
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest) = userService.register(registerRequest)

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        val response = userService.authenticate(authenticationRequest)
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer ${response.token}")
        val redirectUri: URI = UriComponentsBuilder.fromUriString("/api/view/service")
            .build().toUri()

        return ResponseEntity.status(HttpStatus.OK)
            .location(redirectUri)
            .headers(headers)
            .build()
    }
}
