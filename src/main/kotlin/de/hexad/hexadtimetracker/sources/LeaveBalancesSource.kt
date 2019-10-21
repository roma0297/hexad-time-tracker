package de.hexad.hexadtimetracker.sources

import de.hexad.hexadtimetracker.providers.UserInfo
import de.hexad.hexadtimetracker.types.EmployeeLeaveBalanceType
import de.hexad.hexadtimetracker.types.FetchEmployeeLeaveBalancesResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class LeaveBalancesSource(private val zohoApiWebClient: WebClient) {
    companion object {
        private const val FETCH_PROJECTS_REQUEST_PATH = "/leave/getLeaveTypeDetails"
        private const val AUTHENTICATION_TOKEN_KEY = "authtoken"
    }

    fun getLeaveBalanceForEmployee(userId: String): List<EmployeeLeaveBalanceType> {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserInfo

        return zohoApiWebClient.get().uri { uriBuilder ->
            uriBuilder.path(FETCH_PROJECTS_REQUEST_PATH)
            uriBuilder.queryParam(AUTHENTICATION_TOKEN_KEY, principal.token)
            uriBuilder.queryParam("userId", userId)
                    .build()
        }.retrieve().bodyToMono(FetchEmployeeLeaveBalancesResponse::class.java).block()?.leaveBalances ?: listOf()
    }

}