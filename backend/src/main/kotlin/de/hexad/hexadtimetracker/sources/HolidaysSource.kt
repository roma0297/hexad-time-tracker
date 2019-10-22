package de.hexad.hexadtimetracker.sources

import de.hexad.hexadtimetracker.providers.UserInfo
import de.hexad.hexadtimetracker.types.FetchHolidaysResponse
import de.hexad.hexadtimetracker.types.HolidayType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component
class HolidaysSource(private val zohoApiWebClient: WebClient,
                     private val employeesSource: EmployeesSource) {

    companion object {
        private const val FETCH_HOLIDAYS_REQUEST_PATH = "/leave/getHolidays"
        private const val AUTHENTICATION_TOKEN_KEY = "authtoken"
    }

    fun getHolidays(): List<HolidayType> {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserInfo

        return employeesSource.getEmployees()
                .filter { it.status == "Active" }
                .distinctBy { it.workLocation }
                .flatMap {
                    zohoApiWebClient.get().uri { uriBuilder ->
                        uriBuilder.path(FETCH_HOLIDAYS_REQUEST_PATH)
                        uriBuilder.queryParam(AUTHENTICATION_TOKEN_KEY, principal.token)
                        uriBuilder.queryParam("userId", it.recordId)
                                .build()
                    }.retrieve().bodyToMono(FetchHolidaysResponse::class.java).block()?.holidays ?: emptyList()
                }.distinctBy { it.id }
    }

}
