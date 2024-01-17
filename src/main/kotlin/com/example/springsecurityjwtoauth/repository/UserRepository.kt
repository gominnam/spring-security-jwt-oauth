package com.example.springsecurityjwtoauth.repository

import com.example.springsecurityjwtoauth.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<UserEntity, Int> {
    fun findByName(name: String): UserEntity?

    fun findByNameOrEmail(name: String, email: String): UserEntity?

    fun findByEmail(email: String?): Optional<UserEntity>
}