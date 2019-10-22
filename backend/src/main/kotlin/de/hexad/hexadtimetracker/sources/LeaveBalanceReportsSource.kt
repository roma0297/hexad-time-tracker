package de.hexad.hexadtimetracker.sources

import de.hexad.hexadtimetracker.types.FetchLeaveBalanceReportsResponse
import de.hexad.hexadtimetracker.types.LeaveBalanceReportType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class LeaveBalanceReportsSource(private val zohoApiWebClient: WebClient) {
    companion object {
        private const val FETCH_PROJECTS_REQUEST_PATH = "/leave/getLeaveTypeDetails"
        private const val AUTHENTICATION_TOKEN_KEY = "authtoken"
    }

    fun getLeaveBalanceReportsForEmployee(userId: String, authenticationToken: String): List<LeaveBalanceReportType> {
        return zohoApiWebClient.get().uri { uriBuilder ->
            uriBuilder.path(FETCH_PROJECTS_REQUEST_PATH)
            uriBuilder.queryParam(AUTHENTICATION_TOKEN_KEY, authenticationToken)
            uriBuilder.queryParam("userId", userId)
                    .build()
        }.retrieve().bodyToMono(FetchLeaveBalanceReportsResponse::class.java).block()?.reports ?: listOf()
    }

}