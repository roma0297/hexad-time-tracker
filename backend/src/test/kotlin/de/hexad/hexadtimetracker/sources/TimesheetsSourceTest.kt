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

class TimesheetsSourceTest {

    private lateinit var timesheetsSource: TimesheetsSource

    val mockServer = MockWebServer()

    @Before
    fun setUp() {
        timesheetsSource = TimesheetsSource(WebClient.create(mockServer.url("/").toString()))
    }

    @After
    fun afterTests() = mockServer.shutdown()


    @Test
    fun `should return two timesheets`() {
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
                            "owner": "000062012301642037",
                            "formId": "431032100000123901",
                            "nonbillableHours": "80:00",
                            "employeeName": "employeeName1",
                            "erecno": "433292000001642037",
                            "totalHours": "128:00",
                            "toDate": "30-Sep-2019",
                            "description": "",
                            "employeeEmail": "employee1@hexad.de",
                            "approvedBillableHours": "48:00",
                            "employeeId": "0001",
                            "billableHours": "48:00",
                            "recordId": "000000000000000001",
                            "listId": "431062078701323111",
                            "fromDate": "01-Sep-2019",
                            "approvedNonBillableHours": "80:00",
                            "timesheetName": "Timesheet (01-Sep-2019 - 30-Sep-2019)",
                            "currency": "USD",
                            "approvedTotalHours": "128:00",
                            "status": "Approved"
                        },
                        {
                            "owner": "432392000002399001",
                            "formId": "431077800000441001",
                            "nonbillableHours": "168:00",
                            "employeeName": "employeeName2",
                            "erecno": "431099900004319001",
                            "totalHours": "168:00",
                            "toDate": "30-Sep-2019",
                            "description": "",
                            "employeeEmail": "employee2@hexad.de",
                            "approvedBillableHours": "00:00",
                            "employeeId": "0002",
                            "billableHours": "00:00",
                            "recordId": "000000000000000002",
                            "listId": "431062000001821415",
                            "fromDate": "01-Sep-2019",
                            "approvedNonBillableHours": "00:00",
                            "timesheetName": "Timesheet (01-Sep-2019 - 30-Sep-2019)",
                            "currency": "USD",
                            "approvedTotalHours": "00:00",
                            "status": "Pending"
                        }
                    ],
                    "message": "Data fetched successfully",
                    "uri": "/api/timetracker/gettimesheet",
                    "isNextAvailable": true,
                    "status": 0
                }
            }
        """.trimIndent())

        mockServer.enqueue(mockedResponse)

        //when
        val timesheets = timesheetsSource.getTimesheets()

        //then
        assertEquals(timesheets.size, 2)

        val timesheet1 = timesheets[0]

        assertEquals("0001", timesheet1.employeeId)
        assertEquals("employeeName1", timesheet1.employeeName)
        assertEquals("employee1@hexad.de", timesheet1.employeeEmail)
        assertEquals("Timesheet (01-Sep-2019 - 30-Sep-2019)", timesheet1.timesheetName)
        assertEquals("128:00", timesheet1.totalHours)
        assertEquals("128:00", timesheet1.approvedTotalHours)
        assertEquals("48:00", timesheet1.billableHours)
        assertEquals("48:00", timesheet1.approvedBillableHours)
        assertEquals("80:00", timesheet1.approvedNonBillableHours)
        assertEquals("Approved", timesheet1.status)

        val timesheet2 = timesheets[1]

        assertEquals("0002", timesheet2.employeeId)
        assertEquals("employeeName2", timesheet2.employeeName)
        assertEquals("employee2@hexad.de", timesheet2.employeeEmail)
        assertEquals("Timesheet (01-Sep-2019 - 30-Sep-2019)", timesheet2.timesheetName)
        assertEquals("168:00", timesheet2.totalHours)
        assertEquals("00:00", timesheet2.approvedTotalHours)
        assertEquals("00:00", timesheet2.billableHours)
        assertEquals("00:00", timesheet2.approvedBillableHours)
        assertEquals("00:00", timesheet2.approvedNonBillableHours)
        assertEquals("Pending", timesheet2.status)
    }

}