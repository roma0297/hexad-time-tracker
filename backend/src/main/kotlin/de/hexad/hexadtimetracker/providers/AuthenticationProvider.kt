package de.hexad.hexadtimetracker.providers

import de.hexad.hexadtimetracker.sources.AuthenticationSource
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class AuthenticationProvider(private val authenticationSource: AuthenticationSource
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        authentication?.let {
            val email = authentication.name
            val password = authentication.credentials.toString()

            val token = authenticationSource.getAuthenticationToken(email, password)

            return UsernamePasswordAuthenticationToken(UserInfo(email, token), password, listOf(SimpleGrantedAuthority("USER")))
        }

        throw AuthenticationServiceException("Authentication object is equal null")
    }

    override fun supports(authentication: Class<*>?) = authentication?.equals(UsernamePasswordAuthenticationToken::class.java)
            ?: false

}

data class UserInfo(val email: String, val token: String) : Principal {
    override fun getName(): String = email
}