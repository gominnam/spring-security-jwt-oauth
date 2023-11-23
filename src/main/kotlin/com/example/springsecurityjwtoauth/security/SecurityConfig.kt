package com.example.springsecurityjwtoauth.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider
) {
    @Throws(Exception::class)
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? = http
        .csrf { it.disable() } //csrf 비활성화
        .authorizeHttpRequests {
            it
                .requestMatchers(AntPathRequestMatcher("/auth/**"),
                    AntPathRequestMatcher("/h2-console/**")).permitAll() // 요청 인증 없이 허용
//                .requestMatchers(AntPathRequestMatcher("/api/**")).hasRole(Role.USER.name())
                .anyRequest().authenticated() // 그 외 요청은 인증 필요
        }
        .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } } // h2-console 사용을 위한 설정
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 서버에서 세션 상태 저장 안함(STATELESS)
        .authenticationProvider(authenticationProvider) // authenticationProvider를 사용하여 사용자의 인증 정보를 제공(Customizing 한 설정)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java) // jwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 filter 순서
        .logout {
            it
                .logoutUrl("/logout")
                .logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .invalidateHttpSession(true)
        }
        .build() // 설정을 적용하여 SecurityFilterChain을 return
}