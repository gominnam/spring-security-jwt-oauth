package com.example.springsecurityjwtoauth.security.oauth2.user

import com.example.springsecurityjwtoauth.domain.UserEntity
import com.example.springsecurityjwtoauth.exception.OAuth2AuthenticationProcessingException
import com.example.springsecurityjwtoauth.model.AuthProvider
import com.example.springsecurityjwtoauth.repository.UserRepository
import com.example.springsecurityjwtoauth.security.UserPrincipal
import com.example.springsecurityjwtoauth.security.oauth2.user.OAuth2UserInfoFactory.getOAuth2UserInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*


@Service
class CustomOAuth2UserService() : DefaultOAuth2UserService() {
    @Autowired
    private val userRepository: UserRepository? = null

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(oAuth2UserRequest)
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val oAuth2UserInfo =
            getOAuth2UserInfo(oAuth2UserRequest.clientRegistration.registrationId, oAuth2User.attributes)
        if (!StringUtils.hasLength(oAuth2UserInfo.email)) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }
        val userOptional: Optional<UserEntity> = userRepository?.findByEmail(oAuth2UserInfo.email) ?: Optional.empty()
        var user: UserEntity
        if (userOptional.isPresent) {
            user = userOptional.get()
            if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId))) {
                throw OAuth2AuthenticationProcessingException(
                    (("Looks like you're signed up with " +
                            user.getProvider()).toString() + " account. Please use your " + user.getProvider()).toString() +
                            " account to login."
                )
            }
            user = updateExistingUser(user, oAuth2UserInfo)
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo)
        }
        return UserPrincipal.create(user, oAuth2User.attributes)
    }

    private fun registerNewUser(oAuth2UserRequest: OAuth2UserRequest, oAuth2UserInfo: OAuth2UserInfo): UserEntity {
        val user = UserEntity.Builder()
            .provider(AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId))
            .providerId(oAuth2UserInfo.id)
            .name(oAuth2UserInfo.name)
            .email(oAuth2UserInfo.email)
            .imageUrl(oAuth2UserInfo.imageUrl)
            .build()
        return userRepository?.save(user) ?: throw OAuth2AuthenticationProcessingException("Error saving user")
    }

    private fun updateExistingUser(existingUser: UserEntity, oAuth2UserInfo: OAuth2UserInfo): UserEntity {
        existingUser.setName(oAuth2UserInfo.name)
        existingUser.setImageUrl(oAuth2UserInfo.imageUrl)
        return userRepository!!.save(existingUser)
    }
}