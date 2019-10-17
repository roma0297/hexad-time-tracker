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
            "recordId": "431062000001780239",
            "Address": "",
            "First Name": "firstName1",
            "Last Name": "lastName1",
            "Department": "Department1",
            "Location": "Location1",
            "Email ID": "employee1@email.com",
            "Photo": "photoUrl1",
            "Photo_downloadUrl": "photoDownloadUrl1",
            "Added time": "15-Jan-2019 11:15:12",
            "Date of joining": "15-Jan-2019",
            "Modified time": "20-Jan-2019 14:22:43",
            "Employee Role": "Team member",
            "Title": "Software Engineer",
            "Employee Status": "Active",
            "Reporting To": "Superordinate1"
          },
          {
            "EmployeeID": "1570609737680",
            "recordId": "431062000001780030",
            "Address": "",
            "First Name": "firstName2",
            "Last Name": "lastName2",
            "Department": "Department2",
            "Location": "Location2",
            "Email ID": "employee2@email.com",
            "Photo": "photoUrl2",
            "Photo_downloadUrl": "photoDownloadUrl2",
            "Modified time": "20-Jan-2019 14:22:43",
            "Added time": "15-Jan-2019 11:15:12",
            "Date of joining": "15-Jan-2019",
            "Employee Role": "Team member",
            "Title": "Software Engineer",
            "Employee Status": "Inactive",
            "Reporting To": "Superordinate2"
         }]""")

        mockServer.enqueue(mockedResponse)

        //when
        val employees = employeesSource.getEmployees()

        //then
        assertEquals(employees.size, 2)

        val firstEmployee = employees[0]

        assertEquals("1570609737689", firstEmployee.employeeId)
        assertEquals("firstName1", firstEmployee.firstName)
        assertEquals("lastName1", firstEmployee.lastName)
        assertEquals("Department1", firstEmployee.department)
        assertEquals("Location1", firstEmployee.workLocation)
        assertEquals("employee1@email.com", firstEmployee.email)
        assertEquals("photoUrl1", firstEmployee.photoUrl)
        assertEquals("photoDownloadUrl1", firstEmployee.photoDownloadUrl)
        assertEquals("Team member", firstEmployee.employeeRole)
        assertEquals("Software Engineer", firstEmployee.title)
        assertEquals("Active", firstEmployee.status)
        assertEquals("Superordinate1", firstEmployee.superordinate)

        val secondEmployee = employees[1]

        assertEquals("1570609737680", secondEmployee.employeeId)
        assertEquals("firstName2", secondEmployee.firstName)
        assertEquals("lastName2", secondEmployee.lastName)
        assertEquals("Department2", secondEmployee.department)
        assertEquals("Location2", secondEmployee.workLocation)
        assertEquals("employee2@email.com", secondEmployee.email)
        assertEquals("photoUrl2", secondEmployee.photoUrl)
        assertEquals("photoDownloadUrl2", secondEmployee.photoDownloadUrl)
        assertEquals("Team member", secondEmployee.employeeRole)
        assertEquals("Software Engineer", secondEmployee.title)
        assertEquals("Inactive", secondEmployee.status)
        assertEquals("Superordinate2", secondEmployee.superordinate)
    }

}
