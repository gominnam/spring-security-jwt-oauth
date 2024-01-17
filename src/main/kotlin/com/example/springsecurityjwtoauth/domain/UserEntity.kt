package com.example.springsecurityjwtoauth.domain

import com.example.springsecurityjwtoauth.model.AuthProvider
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private var id: Long,

    @Email
    @Column(name = "email", nullable = false)
    private var email: String = "",

    @Column(name = "name", nullable = false)
    private var name: String = "",

    @Column(name = "password", length = Integer.MAX_VALUE)
    private var password: String = "",

    private var imageUrl: String,

    @NotNull
    @Enumerated(EnumType.STRING)
    private var provider: AuthProvider,

    private var providerId: String,

    @Size(max = 20)
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private var role: RoleEnum = RoleEnum.USER

) : UserDetails {
    constructor() : this(0, "", "", "", "", AuthProvider.local, "", RoleEnum.USER) {

    }

    class Builder{
        private var id: Long = 0
        private var email: String = ""
        private var name: String = ""
        private var password: String = ""
        private var imageUrl: String = ""
        private var provider: AuthProvider = AuthProvider.local
        private var providerId: String = ""
        private var role: RoleEnum = RoleEnum.USER

        fun id(id: Long) = apply { this.id = id }
        fun email(email: String) = apply { this.email = email }
        fun name(name: String) = apply { this.name = name }
        fun password(password: String) = apply { this.password = password }
        fun imageUrl(imageUrl: String) = apply { this.imageUrl = imageUrl }
        fun provider(provider: AuthProvider) = apply { this.provider = provider }
        fun providerId(providerId: String) = apply { this.providerId = providerId }
        fun role(role: RoleEnum) = apply { this.role = role }

        fun build() = UserEntity(id, email, name, password, imageUrl, provider, providerId, role)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return mutableListOf(SimpleGrantedAuthority(this.role.name))
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.name
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun getId(): Long {
        return id
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setEmail(name: String?) {
        this.email = email
    }

    fun setImageUrl(imageUrl: String?) {
        if (imageUrl != null) {
            this.imageUrl = imageUrl
        }
    }

    fun getProvider(): AuthProvider {
        return provider
    }

    fun setProvider(valueOf: AuthProvider) {
        this.provider = provider
    }

    fun getProviderId(): String {
        return providerId
    }

    fun setProviderId(id: String?) {
        this.providerId = providerId
    }

    fun getEmail(): String {
        return this.email
    }
}


enum class RoleEnum {
    USER, ADMIN
}