package de.hexad.hexadtimetracker.sources

import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Component
class AuthenticationSource(private val zohoAuthTokenWebClient: WebClient) {

    companion object {
        private const val CREATE_TOKEN_REQUEST_PATH = "/apiauthtoken/nb/create"
        private const val SCOPE_KEY = "SCOPE"
        private const val ZOHO_PEOPLE_SCOPE = "Zohopeople/peopleapi"
        private const val EMAIL_KEY = "EMAIL_ID"
        private const val PASSWORD_KEY = "PASSWORD"
        private const val RESULT_RESPONSE_KEY = "RESULT"
        private const val AUTH_TOKEN_RESPONSE_KEY = "AUTHTOKEN="
    }

    fun getAuthenticationToken(email: String, password: String): String {
        zohoAuthTokenWebClient.post()
                .uri(CREATE_TOKEN_REQUEST_PATH).body(BodyInserters.fromFormData(SCOPE_KEY, ZOHO_PEOPLE_SCOPE)
                        .with(EMAIL_KEY, email)
                        .with(PASSWORD_KEY, password))
                .retrieve()
                .bodyToMono(String::class.java).block()?.let {
                    if (it.contains("$RESULT_RESPONSE_KEY=TRUE")) {
                        return it.substringAfter(AUTH_TOKEN_RESPONSE_KEY).substringBefore("\n$RESULT_RESPONSE_KEY=TRUE")
                    }
                }

        throw AuthenticationServiceException("Unable to authenticate user")
    }
}