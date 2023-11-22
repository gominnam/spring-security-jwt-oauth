package com.example.springsecurityjwtoauth.controller

import com.example.springsecurityjwtoauth.model.authentication.AuthenticationRequest
import com.example.springsecurityjwtoauth.model.authentication.RegisterRequest
import com.example.springsecurityjwtoauth.service.UserService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val userService: UserService,
) {
    @ResponseStatus(CREATED)
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest) = userService.register(registerRequest)

    @ResponseStatus(OK)
    @PostMapping("/authenticate")
    fun authenticate(@RequestBody authenticationRequest: AuthenticationRequest) =
        userService.authenticate(authenticationRequest)

}
