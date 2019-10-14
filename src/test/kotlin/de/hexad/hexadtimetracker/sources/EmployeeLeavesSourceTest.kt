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
                    "Employee_ID": "Karan John 1",
                    "DayDetails": {
                      "07-Mar-2020": {
                        "LeaveCount": "0.5",
                        "Session": 1
                      },
                      "04-Mar-2020": {
                        "LeaveCount": "1"
                      },
                      "06-Mar-2020": {
                        "LeaveCount": "0.25",
                        "Session": 1
                      },
                      "08-Mar-2020": {
                        "LeaveCount": "0.5",
                        "Session": 1
                      },
                      "09-Mar-2020": {
                        "LeaveCount": "0.5",
                        "Session": 2
                      },
                      "05-Mar-2020": {
                        "LeaveCount": "0.5",
                        "Session": 1
                      }
                    },
                    "Leavetype.ID": "413124000000645719",
                    "From": "04-Mar-2020",
                    "Unit": "Day",
                    "ApprovalStatus": "Pending",
                    "Reasonforleave": "Vacation",
                    "Daystaken": "2.25",
                    "TeamEmailID": "teamemail@gmail.com",
                    "Department": "HR",
                    "Leavetype": "Annual leave",
                    "To": "09-Mar-2020",
                    "Department.ID": "413124680000117005",
                    "Employee_ID.ID": "413124000780117005",
                    "DateOfRequest": "14-Aug-2019"
                  }
                ],
                "message": "Data fetched successfully",
                "uri": "/api/forms/leave/getDataByID",
                "status": 0
              }  
            }""".trimIndent())

        mockServer.enqueue(mockedResponse)

        //when
        val leaves = employeeLeavesSource.getEmployeeLeaves("431062000001278239")
        //then

        Assertions.assertEquals(leaves.size, 1)

        val leave = leaves[0]

        Assertions.assertEquals("teamemail@gmail.com", leave.email)
        Assertions.assertEquals(LocalDate.of(2020, Month.MARCH, 4), leave.startDate)
        Assertions.assertEquals(LocalDate.of(2020, Month.MARCH, 9), leave.endDate)
        Assertions.assertEquals("Annual leave", leave.leaveType)
    }


}