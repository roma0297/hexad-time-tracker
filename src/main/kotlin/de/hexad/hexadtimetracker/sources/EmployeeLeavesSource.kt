package de.hexad.hexadtimetracker.sources

import de.hexad.hexadtimetracker.providers.UserInfo
import de.hexad.hexadtimetracker.types.EmployeeLeaveType
import de.hexad.hexadtimetracker.types.FetchEmployeeLeavesResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class EmployeeLeavesSource(private val zohoApiWebClient: WebClient) {

    companion object {
        private const val FETCH_EMPLOYEE_LEAVES_REQUEST_PATH = "/forms/leave/getDataByID"
        private const val AUTHENTICATION_TOKEN_KEY = "authtoken"
        private const val RECORD_ID_REQUEST_PARAM_KEY = "recordId"
    }

    fun getEmployeeLeaves(recordId: String): List<EmployeeLeaveType> {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserInfo

        return zohoApiWebClient.get().uri { uriBuilder ->
            uriBuilder.path(FETCH_EMPLOYEE_LEAVES_REQUEST_PATH)
            uriBuilder.queryParam(AUTHENTICATION_TOKEN_KEY, principal.token)
            uriBuilder.queryParam(RECORD_ID_REQUEST_PARAM_KEY, recordId)
                    .build()
        }.retrieve().bodyToMono(FetchEmployeeLeavesResponse::class.java).block()?.leaves ?: emptyList()
    }

}