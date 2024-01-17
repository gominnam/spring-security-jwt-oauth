package com.example.springsecurityjwtoauth.model.authentication

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String
)