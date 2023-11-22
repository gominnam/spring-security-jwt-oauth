package com.example.springsecurityjwtoauth.repository

import com.example.springsecurityjwtoauth.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Int> {
    fun findByUsername(username: String): UserEntity?

    fun findByUsernameOrEmail(username: String, email: String): UserEntity?
}