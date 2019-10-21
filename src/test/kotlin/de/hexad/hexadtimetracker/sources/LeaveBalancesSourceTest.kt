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

class LeaveBalancesSourceTest {
    companion object {
        private const val DELTA = 1e-6
    }

    private lateinit var leaveBalancesSource: LeaveBalancesSource

    private val mockServer = MockWebServer()

    @Before
    fun setUp() {
        leaveBalancesSource = LeaveBalancesSource(WebClient.create(mockServer.url("/").toString()))
    }

    @After
    fun afterTests() = mockServer.shutdown()


    @Test
    fun `should return two leave balances`() {
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
                            "isHalfDayEnabled": false,
                            "Color": "#64d5fd",
                            "AvailedCount": 0,
                            "isQuarterDayEnabled": false,
                            "DoNotDisplayBalance": false,
                            "isQuarterfDayEnabled": false,
                            "Unit": "Days",
                            "showFileUploadAfter": 0,
                            "version": 1,
                            "Name": "Birth of your child",
                            "PermittedCount": 1,
                            "BalanceCount": "1.0",
                            "isHourEnabled": false,
                            "Id": "431062000000185013"
                        },
                        {
                            "isHalfDayEnabled": false,
                            "Color": "#64d5fd",
                            "AvailedCount": 0,
                            "isQuarterDayEnabled": false,
                            "DoNotDisplayBalance": false,
                            "isQuarterfDayEnabled": false,
                            "Unit": "Days",
                            "showFileUploadAfter": 0.25,
                            "version": 1,
                            "Name": "Child Sick Leave",
                            "PermittedCount": 20,
                            "BalanceCount": "20.0",
                            "isHourEnabled": false,
                            "Id": "431062000000277003"
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
        val leaveBalances = leaveBalancesSource.getLeaveBalanceForEmployee("")

        //then
        assertEquals(leaveBalances.size, 2)

        val leaveBalance1 = leaveBalances[0]
        assertEquals("431062000000185013", leaveBalance1.id)
        assertEquals("Birth of your child", leaveBalance1.name)
        assertEquals(1, leaveBalance1.permittedCount)
        assertEquals(1.0, leaveBalance1.balanceCount, DELTA)
        assertEquals(0, leaveBalance1.availedCount)
        assertEquals("Days", leaveBalance1.unit)
        assertEquals(false, leaveBalance1.isHalfDayEnabled)
        assertEquals(false, leaveBalance1.isQuarterDayEnabled)
        assertEquals(false, leaveBalance1.isHourEnabled)
        assertEquals("#64d5fd", leaveBalance1.colorCode)
        assertEquals(false, leaveBalance1.isBalanceHidden)
        assertEquals(0.0, leaveBalance1.showFileUploadAfter, DELTA)
        assertEquals(1, leaveBalance1.version)

        val leaveBalance2 = leaveBalances[1]
        assertEquals("431062000000277003", leaveBalance2.id)
        assertEquals("Child Sick Leave", leaveBalance2.name)
        assertEquals(20, leaveBalance2.permittedCount)
        assertEquals(20.0, leaveBalance2.balanceCount, DELTA)
        assertEquals(0, leaveBalance2.availedCount)
        assertEquals("Days", leaveBalance2.unit)
        assertEquals(false, leaveBalance2.isHalfDayEnabled)
        assertEquals(false, leaveBalance2.isQuarterDayEnabled)
        assertEquals(false, leaveBalance2.isHourEnabled)
        assertEquals("#64d5fd", leaveBalance2.colorCode)
        assertEquals(false, leaveBalance2.isBalanceHidden)
        assertEquals(0.25, leaveBalance2.showFileUploadAfter, DELTA)
        assertEquals(1, leaveBalance2.version)
    }
}