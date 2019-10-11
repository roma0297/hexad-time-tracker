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


class EmployeesSourceTest {

    lateinit var employeesSource: EmployeesSource

    val mockServer = MockWebServer()

    @Before
    fun setUp() {
        employeesSource = EmployeesSource(WebClient.create(mockServer.url("/").toString()))
    }

    @After
    fun afterTests() {
        mockServer.shutdown()
    }

    @Test
    fun `should return two employees`() {
        //given
        val principal = mockk<UserInfo>()
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication.principal } returns principal
        every { principal.token } returns "e07119171812c29b3a0dacdb79a57e3f"

        val mockedResponse = MockResponse()
        mockedResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody("""[
          {
            "EmployeeID": "1570609737689",
            "Address": "",
            "Added time": "07-Oct-2019 12:03:33",
            "First Name": "first one",
            "Last Name": "last one",
            "Department": "Engineering",
            "Other Email": "",
            "Location": "Berlin"
          },
          {
            "EmployeeID": "1570609737680",
            "Address": "",
            "Added time": "07-Oct-2019 12:03:33",
            "Department": "Engineering",
            "First Name": "first second",
            "Last Name": "last second",
            "Location": "Berlin"
         }]""")

        mockServer.enqueue(mockedResponse)

        //when
        val employees = employeesSource.getEmployees()
        //then

        assertEquals(employees.size, 2)

        val firstEmployee = employees.get(0)

        assertEquals(firstEmployee.employeeId, "1570609737689")
        assertEquals(firstEmployee.firstName, "first one")
        assertEquals(firstEmployee.lastName, "last one")
        assertEquals(firstEmployee.department, "Engineering")
        assertEquals(firstEmployee.workLocation, "Berlin")


        val secondEmployee = employees.get(1)

        assertEquals(secondEmployee.employeeId, "1570609737680")
        assertEquals(secondEmployee.firstName, "first second")
        assertEquals(secondEmployee.lastName, "last second")
        assertEquals(secondEmployee.department, "Engineering")
        assertEquals(secondEmployee.workLocation, "Berlin")
    }

}
