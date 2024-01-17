package com.example.springsecurityjwtoauth.controller

import com.example.springsecurityjwtoauth.security.oauth2.user.CustomOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/oauth")
class OauthController(private val customOAuth2UserService: CustomOAuth2UserService) {
    @GetMapping("/callback/{registrationId}")
    fun googleLogin(@RequestParam code: String?, @PathVariable registrationId: String?) {
        var test = code
//        customOAuth2UserService.login(code, registrationId)
    }
}