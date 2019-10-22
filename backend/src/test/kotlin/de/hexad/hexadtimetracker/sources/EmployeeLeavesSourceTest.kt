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
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate
import java.time.Month

class EmployeeLeavesSourceTest {
    lateinit var employeeLeavesSource: EmployeeLeavesSource

    val mockServer = MockWebServer()

    @Before
    fun setUp() {
        employeeLeavesSource = EmployeeLeavesSource(WebClient.create(mockServer.url("/").toString()))
    }

    @After
    fun afterTests() {
        mockServer.shutdown()
    }

    @Test
    fun `should return one leave`() {
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
                    "Medical_Certificate_downloadUrl": "",
                    "ZP_Leave_File_Upload": "",
                    "Employee_ID": "Farzaneh Sabzi 1182",
                    "DayDetails": {
                      "14-Oct-2019": {
                        "LeaveCount": "1.0"
                      }
                    },
                    "Leavetype.ID": "431062000000185043",
                    "From": "14-Oct-2019",
                    "Unit": "Day",
                    "ApprovalStatus": "Approved",
                    "Daystaken": "1.0",
                    "Reasonforleave": "",
                    "TeamEmailID": "",
                    "Medical_Certificate": "",
                    "Leavetype": "Privilage Leave",
                    "ApprovalTime": "02-Oct-2019 09:42:57",
                    "To": "14-Oct-2019",
                    "Employee_ID.ID": "431062000001599911",
                    "DateOfRequest": "01-Oct-2019",
                    "ZP_Leave_File_Upload_downloadUrl": ""
                  }
                ],
                "message": "Data fetched successfully",
                "uri": "/api/forms/leave/getDataByID",
                "status": 0
              }
            }
            """.trimIndent())

        mockServer.enqueue(mockedResponse)

        //when
        val leaveId = "431062000001599911"
        val leave = employeeLeavesSource.getEmployeeLeaves(leaveId)[0]

        //then
        Assertions.assertEquals(leaveId, leave.leaveId)
        Assertions.assertEquals(LocalDate.of(2019, Month.OCTOBER, 14), leave.from)
        Assertions.assertEquals(LocalDate.of(2019, Month.OCTOBER, 14), leave.to)
    }


}