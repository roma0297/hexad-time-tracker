package de.hexad.hexadtimetracker.sources

import de.hexad.hexadtimetracker.providers.UserInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.reactive.function.client.WebClient

class ProjectsSourceTest {
    companion object {
        private const val DELTA = 1e-6
    }

    private lateinit var projectsSource: ProjectsSource

    private val mockServer = MockWebServer()

    @Before
    fun setUp() {
        projectsSource = ProjectsSource(WebClient.create(mockServer.url("/").toString()))
    }

    @After
    fun afterTests() = mockServer.shutdown()


    @Test
    fun `should return two projects`() {
        //given
        val principal = mockk<UserInfo>()
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication.principal } returns principal
        every { principal.token } returns "e07119171812c29b3a0dacdb79a57e3f"

        val mockedResponse = MockResponse()
        mockedResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody("""
            {
                "response": {
                    "result": [
                        {
                            "projectStatus": "In-Progress",
                            "ownerName": " ",
                            "projectCost": 0,
                            "isDeleteAllowed": true,
                            "projectName": "Balboa/VCF",
                            "ownerId": "431062000000355141",
                            "projectId": "431062000000603003"
                        },
                        {
                            "projectManager": "431062000000212497",
                            "projectStatus": "In-Progress",
                            "ownerName": "Dennis Berke",
                            "projectManagerName": "Dennis Berke",
                            "isDeleteAllowed": true,
                            "projectName": "BI-Projekt (Übergreifend)",
                            "ownerId": "431062000000212497",
                            "projectId": "431062000000417195"
                        }
                    ],
                    "message": "Data fetched successfully",
                    "uri": "/api/timetracker/getprojects",
                    "status": 0
                }
            }
        """.trimIndent())

        mockServer.enqueue(mockedResponse)

        //when
        val projects = projectsSource.getProjects()

        //then
        assertEquals(projects.size, 2)

        val project1 = projects[0]
        assertEquals("431062000000603003", project1.id)
        assertEquals("Balboa/VCF", project1.name)
        assertEquals("In-Progress", project1.status)
        assertEquals("431062000000355141", project1.ownerId)
        assertEquals(" ", project1.ownerName)
        assertEquals("", project1.projectManagerId)
        assertEquals("", project1.projectManagerName)
        assertEquals(true, project1.isDeleteAllowed)
        assertEquals(0.0, project1.projectCost, DELTA)

        val project2 = projects[1]
        assertEquals("431062000000417195", project2.id)
        assertEquals("BI-Projekt (Übergreifend)", project2.name)
        assertEquals("In-Progress", project2.status)
        assertEquals("431062000000212497", project2.ownerId)
        assertEquals("Dennis Berke", project2.ownerName)
        assertEquals("431062000000212497", project2.projectManagerId)
        assertEquals("Dennis Berke", project2.projectManagerName)
        assertEquals(true, project2.isDeleteAllowed)
        assertEquals(0.0, project2.projectCost, DELTA)
    }
}