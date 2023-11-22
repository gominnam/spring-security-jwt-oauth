package com.example.springsecurityjwtoauth.model.authentication

data class AuthenticationRequest(
    val username: String,
    val password: String
)