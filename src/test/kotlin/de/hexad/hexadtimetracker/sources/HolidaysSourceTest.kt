package de.hexad.hexadtimetracker.sources

import de.hexad.hexadtimetracker.providers.UserInfo
import de.hexad.hexadtimetracker.types.EmployeeType
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId

class HolidaysSourceTest {

    val employeesSource = mockk<EmployeesSource>()

    lateinit var holidaysSource: HolidaysSource

    val mockServer = MockWebServer()

    @Before
    fun setUp() {
        holidaysSource = HolidaysSource(
                zohoApiWebClient = WebClient.create(mockServer.url("/").toString()),
                employeesSource = employeesSource)
    }

    @After
    fun afterTests() {
        mockServer.shutdown()
    }

    @Test
    fun `should return list of all holidays without duplicates`() {
        //given
        val principal = mockk<UserInfo>()
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication.principal } returns principal
        every { principal.token } returns "e07119171812c29b3a0dacdb79a57e3f"

        val employee1 = mockk<EmployeeType>()
        every { employee1.recordId } returns "000000000000000001"
        every { employee1.status } returns "Active"
        every { employee1.workLocation } returns "Berlin"
        val employee2 = mockk<EmployeeType>()
        every { employee2.recordId } returns "000000000000000002"
        every { employee2.status } returns "Active"
        every { employee2.workLocation } returns "Wolfsburg"
        every { employeesSource.getEmployees() } returns listOf(employee1, employee2)

        val mockedResponse1 = MockResponse()
        mockedResponse1.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        mockedResponse1.setResponseCode(200)
        mockedResponse1.setBody("""
            {
                "response": {
                    "result": [
                        {
                            "fromDate": "2019-01-01",
                            "isRestrictedHoliday": false,
                            "Remarks": "",
                            "toDate": "2019-01-01",
                            "LocationId": "",
                            "Id": 431062000000300001,
                            "LocationName": "",
                            "Name": "New Year's Day"
                        },
                        {
                            "fromDate": "2019-03-08",
                            "isRestrictedHoliday": false,
                            "Remarks": "",
                            "toDate": "2019-03-08",
                            "LocationId": "431062000000131067",
                            "Id": 431062000000723555,
                            "LocationName": "Berlin",
                            "Name": "International Women day"
                        }
                    ],
                    "message": "Data fetched successfully",
                    "uri": "/api/leave/getHolidays",
                    "status": 0
                }
            }
        """.trimIndent())

        val mockedResponse2 = MockResponse()
        mockedResponse2.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        mockedResponse2.setResponseCode(200)
        mockedResponse2.setBody("""
            {
                "response": {
                    "result": [
                        {
                            "fromDate": "2019-01-01",
                            "isRestrictedHoliday": false,
                            "Remarks": "",
                            "toDate": "2019-01-01",
                            "LocationId": "",
                            "Id": 431062000000300001,
                            "LocationName": "",
                            "Name": "New Year's Day"
                        },
                        {
                            "fromDate": "2019-05-30",
                            "isRestrictedHoliday": false,
                            "Remarks": "",
                            "toDate": "2019-05-30",
                            "LocationId": "431062000000124121;431062000000131067;431062000000180133",
                            "Id": 431062000000300031,
                            "LocationName": "Wolfsburg;Berlin;Langen",
                            "Name": "Ascension Day"
                        }
                    ],
                    "message": "Data fetched successfully",
                    "uri": "/api/leave/getHolidays",
                    "status": 0
                }
            }
        """.trimIndent())

        mockServer.enqueue(mockedResponse1)
        mockServer.enqueue(mockedResponse2)

        //when
        val holidays = holidaysSource.getHolidays()

        //then
        assertEquals(3, holidays.size)

        val holiday1 = holidays.find { it.id == "431062000000300001" } ?: throw AssertionError()
        assertEquals("New Year's Day", holiday1.name)
        assertEquals(LocalDate.of(2019, Month.JANUARY, 1), holiday1.fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        assertEquals(LocalDate.of(2019, Month.JANUARY, 1), holiday1.toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        assertEquals(false, holiday1.isRestrictedHoliday)
        assertEquals("", holiday1.remarks)
        assertEquals("", holiday1.locationName)


        val holiday2 = holidays.find { it.id == "431062000000723555" } ?: throw AssertionError()
        assertEquals("International Women day", holiday2.name)
        assertEquals(LocalDate.of(2019, Month.MARCH, 8), holiday2.fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        assertEquals(LocalDate.of(2019, Month.MARCH, 8), holiday2.toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        assertEquals(false, holiday2.isRestrictedHoliday)
        assertEquals("", holiday2.remarks)
        assertEquals("Berlin", holiday2.locationName)

        val holiday3 = holidays.find { it.id == "431062000000300031" } ?: throw AssertionError()
        assertEquals("Ascension Day", holiday3.name)
        assertEquals(LocalDate.of(2019, Month.MAY, 30), holiday3.fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        assertEquals(LocalDate.of(2019, Month.MAY, 30), holiday3.toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        assertEquals(false, holiday3.isRestrictedHoliday)
        assertEquals("", holiday3.remarks)
        assertEquals("Wolfsburg;Berlin;Langen", holiday3.locationName)
    }

    @Test
    fun `should ignore users with 'inactive' status`() {
        //given
        val principal = mockk<UserInfo>()
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication.principal } returns principal
        every { principal.token } returns "e07119171812c29b3a0dacdb79a57e3f"

        val employee = mockk<EmployeeType>()
        every { employee.recordId } returns "000000000000000001"
        every { employee.status } returns "Inactive"
        every { employee.workLocation } returns "Berlin"

        every { employeesSource.getEmployees() } returns listOf(employee)

        //when
        val holidays = holidaysSource.getHolidays()

        //then
        assertTrue(holidays.isEmpty())
    }

}