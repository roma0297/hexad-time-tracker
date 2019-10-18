package de.hexad.hexadtimetracker.sources

import de.hexad.hexadtimetracker.providers.UserInfo
import de.hexad.hexadtimetracker.types.FetchProjectsResponse
import de.hexad.hexadtimetracker.types.ProjectType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class ProjectsSource(private val zohoApiWebClient: WebClient) {
    companion object {
        private const val FETCH_PROJECTS_REQUEST_PATH = "/timetracker/getprojects"
        private const val AUTHENTICATION_TOKEN_KEY = "authtoken"
    }

    fun getProjects(): List<ProjectType> {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserInfo

        return zohoApiWebClient.get().uri { uriBuilder ->
            uriBuilder.path(FETCH_PROJECTS_REQUEST_PATH)
            uriBuilder.queryParam(AUTHENTICATION_TOKEN_KEY, principal.token)
            uriBuilder.queryParam("assignedTo", "all")
                    .build()
        }.retrieve().bodyToMono(FetchProjectsResponse::class.java).block()?.projects ?: listOf()
    }

}