package com.example.springsecurityjwtoauth.service

import com.example.springsecurityjwtoauth.domain.UserEntity
import com.example.springsecurityjwtoauth.exception.RegisteredException
import com.example.springsecurityjwtoauth.model.authentication.AuthenticationRequest
import com.example.springsecurityjwtoauth.model.authentication.AuthenticationResponse
import com.example.springsecurityjwtoauth.model.authentication.RegisterRequest
import com.example.springsecurityjwtoauth.repository.UserRepository
import com.example.springsecurityjwtoauth.security.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    fun register(registerRequest: RegisterRequest): AuthenticationResponse {

        if (userRepository.findByNameOrEmail(registerRequest.name, registerRequest.email) != null) {
            throw RegisteredException()
        }

        val user = UserEntity.Builder().email(registerRequest.email).name(registerRequest.name).password(
            passwordEncoder.encode(registerRequest.password)
        ).build()

        userRepository.save(user)

        val token = jwtService.generateToken(user)

        return AuthenticationResponse(token)
    }

    fun authenticate(authenticationRequest: AuthenticationRequest): AuthenticationResponse {

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.username,
                authenticationRequest.password
            )
        )

        val user = userRepository.findByName(authenticationRequest.username)

        return AuthenticationResponse(jwtService.generateToken(user!!))
    }
}