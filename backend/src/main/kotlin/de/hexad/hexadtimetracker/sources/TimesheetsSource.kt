package de.hexad.hexadtimetracker.sources

import de.hexad.hexadtimetracker.providers.UserInfo
import de.hexad.hexadtimetracker.types.FetchTimesheetsResponse
import de.hexad.hexadtimetracker.types.TimesheetType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class TimesheetsSource(private val zohoApiWebClient: WebClient) {

    companion object {
        private const val FETCH_TIMESHEETS_REQUEST_PATH = "/timetracker/gettimesheet"
        private const val AUTHENTICATION_TOKEN_KEY = "authtoken"
        private const val USER_KEY = "user"
    }

    fun getTimesheets(): List<TimesheetType> {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserInfo

        return zohoApiWebClient.get().uri { uriBuilder ->
            uriBuilder.path(FETCH_TIMESHEETS_REQUEST_PATH)
            uriBuilder.queryParam(AUTHENTICATION_TOKEN_KEY, principal.token)
            uriBuilder.queryParam(USER_KEY, "all")
                    .build()
        }.retrieve().bodyToMono(FetchTimesheetsResponse::class.java).block()?.timesheets ?: emptyList()
    }

}