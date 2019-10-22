package de.hexad.hexadtimetracker.sources

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

class LeaveBalanceReportsSourceTest {
    companion object {
        private const val DELTA = 1e-6
    }

    private lateinit var leaveBalanceReportsSource: LeaveBalanceReportsSource

    private val mockServer = MockWebServer()

    @Before
    fun setUp() {
        leaveBalanceReportsSource = LeaveBalanceReportsSource(WebClient.create(mockServer.url("/").toString()))
    }

    @After
    fun afterTests() = mockServer.shutdown()


    @org.junit.Test
    fun `should return two leave balances`() {
        //given
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
        val leaveBalanceReports = leaveBalanceReportsSource.getLeaveBalanceReportsForEmployee("userId", "e07119171812c29b3a0dacdb79a57e3f")

        //then
        assertEquals(leaveBalanceReports.size, 2)

        val leaveBalanceReport1 = leaveBalanceReports[0]
        assertEquals("431062000000185013", leaveBalanceReport1.id)
        assertEquals("Birth of your child", leaveBalanceReport1.name)
        assertEquals(1, leaveBalanceReport1.permittedCount)
        assertEquals(1.0, leaveBalanceReport1.balanceCount, DELTA)
        assertEquals(0, leaveBalanceReport1.availedCount)
        assertEquals("Days", leaveBalanceReport1.unit)
        assertEquals(false, leaveBalanceReport1.isHalfDayEnabled)
        assertEquals(false, leaveBalanceReport1.isQuarterDayEnabled)
        assertEquals(false, leaveBalanceReport1.isHourEnabled)
        assertEquals("#64d5fd", leaveBalanceReport1.colorCode)
        assertEquals(false, leaveBalanceReport1.isBalanceHidden)
        assertEquals(0.0, leaveBalanceReport1.showFileUploadAfter, DELTA)
        assertEquals(1, leaveBalanceReport1.version)

        val leaveBalanceReport2 = leaveBalanceReports[1]
        assertEquals("431062000000277003", leaveBalanceReport2.id)
        assertEquals("Child Sick Leave", leaveBalanceReport2.name)
        assertEquals(20, leaveBalanceReport2.permittedCount)
        assertEquals(20.0, leaveBalanceReport2.balanceCount, DELTA)
        assertEquals(0, leaveBalanceReport2.availedCount)
        assertEquals("Days", leaveBalanceReport2.unit)
        assertEquals(false, leaveBalanceReport2.isHalfDayEnabled)
        assertEquals(false, leaveBalanceReport2.isQuarterDayEnabled)
        assertEquals(false, leaveBalanceReport2.isHourEnabled)
        assertEquals("#64d5fd", leaveBalanceReport2.colorCode)
        assertEquals(false, leaveBalanceReport2.isBalanceHidden)
        assertEquals(0.25, leaveBalanceReport2.showFileUploadAfter, DELTA)
        assertEquals(1, leaveBalanceReport2.version)
    }
}