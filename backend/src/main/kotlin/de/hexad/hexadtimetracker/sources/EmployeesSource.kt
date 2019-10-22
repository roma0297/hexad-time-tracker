package de.hexad.hexadtimetracker.sources

import de.hexad.hexadtimetracker.providers.UserInfo
import de.hexad.hexadtimetracker.types.EmployeeType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class EmployeesSource(private val zohoApiWebClient: WebClient) {
    companion object {
        private const val FETCH_EMPLOYEE_RECORDS_REQUEST_PATH = "/forms/P_EmployeeView/records"
        private const val AUTHENTICATION_TOKEN_KEY = "authtoken"
    }

    fun getEmployees(): List<EmployeeType> {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserInfo

        return ArrayList(zohoApiWebClient.get().uri { uriBuilder ->
            uriBuilder.path(FETCH_EMPLOYEE_RECORDS_REQUEST_PATH)
            uriBuilder.queryParam(AUTHENTICATION_TOKEN_KEY, principal.token)
                    .build()
        }.retrieve().bodyToFlux(EmployeeType::class.java).collectList().block() ?: mutableListOf())
    }

}