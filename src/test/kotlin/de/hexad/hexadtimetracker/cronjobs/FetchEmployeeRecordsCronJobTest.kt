package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.converters.EmployeeTypeToEmployeeModelConverter
import de.hexad.hexadtimetracker.models.EmployeeModel
import de.hexad.hexadtimetracker.repositories.EmployeeRepository
import de.hexad.hexadtimetracker.sources.EmployeesSource
import de.hexad.hexadtimetracker.types.EmployeeType
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class FetchEmployeeRecordsCronJobTest {

    @MockK
    var employeeRepository = mockk<EmployeeRepository>(relaxed = true)
    @MockK
    var employeesSource = mockk<EmployeesSource>()
    @MockK
    var employeeTypeToEmployeeModelConverter = mockk<EmployeeTypeToEmployeeModelConverter>()

    var fetchEmployeeRecordsCronJob = FetchEmployeeRecordsCronJob(
            employeesSource = employeesSource,
            employeeRepository = employeeRepository,
            employeeTypeToEmployeeModelConverter = employeeTypeToEmployeeModelConverter)

    @Test
    fun `should retrieve all employee DTOs, convert them to models and store in the DB`() {
        //given
        val employeeDto1 = mockk<EmployeeType>()
        val employeeDto2 = mockk<EmployeeType>()

        every { employeesSource.getEmployees() } returns listOf(employeeDto1, employeeDto2)
        every { employeeTypeToEmployeeModelConverter.convert(employeeDto1) } returns EmployeeModel(
                employeeId = "0001",
                recordId = "000000000000000001",
                firstName = "FirstName1",
                lastName = "LastName1",
                email = "test.email1@hexad.de",
                photoUrl = "photoUrl1",
                photoDownloadUrl = "photoDownloadUrl1",
                lastModificationTime = LocalDateTime.of(2019, Month.AUGUST, 11, 12, 22),
                creationTime = LocalDateTime.of(2019, Month.JUNE, 22, 11, 3, 46),
                joiningDate = LocalDate.of(2019, Month.JUNE, 23),
                employeeRole = "Employee",
                employeeTitle = "Office Management Assistant",
                department = "department1",
                employeeStatus = "Active",
                location = "Wolfsburg",
                superordinate = "Test User 0239"

        )
        every { employeeTypeToEmployeeModelConverter.convert(employeeDto2) } returns EmployeeModel(
                employeeId = "1019",
                recordId = "000000000000000002",
                firstName = "FirstName2",
                lastName = "LastName2",
                email = "test.email2@hexad.de",
                photoUrl = "photoUrl2",
                photoDownloadUrl = "photoDownloadUrl2",
                lastModificationTime = LocalDateTime.of(2018, Month.FEBRUARY, 14, 12, 22),
                creationTime = LocalDateTime.of(2018, Month.JANUARY, 17, 14, 39, 2),
                joiningDate = LocalDate.of(2018, Month.JANUARY, 17),
                employeeRole = "Employee",
                employeeTitle = "Software Engineer",
                department = "department2",
                employeeStatus = "Inactive",
                location = "Berlin",
                superordinate = "Test User 0030"
        )

        //when
        fetchEmployeeRecordsCronJob.perform()

        //then
        val employeesCapturingSlot = slot<List<EmployeeModel>>()

        verify(exactly = 1) { employeeRepository.saveAll(capture(employeesCapturingSlot)) }

        val employees = employeesCapturingSlot.captured
        assertEquals(2, employees.size)

        val firstEmployee = employees[0]
        assertEquals("0001", firstEmployee.employeeId)
        assertEquals("000000000000000001", firstEmployee.recordId)
        assertEquals("FirstName1", firstEmployee.firstName)
        assertEquals("LastName1", firstEmployee.lastName)
        assertEquals("test.email1@hexad.de", firstEmployee.email)
        assertEquals("photoUrl1", firstEmployee.photoUrl)
        assertEquals("photoDownloadUrl1", firstEmployee.photoDownloadUrl)
        assertEquals(LocalDateTime.of(2019, Month.AUGUST, 11, 12, 22), firstEmployee.lastModificationTime)
        assertEquals(LocalDateTime.of(2019, Month.JUNE, 22, 11, 3, 46), firstEmployee.creationTime)
        assertEquals(LocalDate.of(2019, Month.JUNE, 23), firstEmployee.joiningDate)
        assertEquals("Employee", firstEmployee.employeeRole)
        assertEquals("Office Management Assistant", firstEmployee.employeeTitle)
        assertEquals("department1", firstEmployee.department)
        assertEquals("Active", firstEmployee.employeeStatus)
        assertEquals("Wolfsburg", firstEmployee.location)
        assertEquals("Test User 0239", firstEmployee.superordinate)

        val secondEmployee = employees[1]
        assertEquals("1019", secondEmployee.employeeId)
        assertEquals("000000000000000002", secondEmployee.recordId)
        assertEquals("FirstName2", secondEmployee.firstName)
        assertEquals("LastName2", secondEmployee.lastName)
        assertEquals("test.email2@hexad.de", secondEmployee.email)
        assertEquals("photoUrl2", secondEmployee.photoUrl)
        assertEquals("photoDownloadUrl2", secondEmployee.photoDownloadUrl)
        assertEquals(LocalDateTime.of(2018, Month.FEBRUARY, 14, 12, 22), secondEmployee.lastModificationTime)
        assertEquals(LocalDateTime.of(2018, Month.JANUARY, 17, 14, 39, 2), secondEmployee.creationTime)
        assertEquals(LocalDate.of(2018, Month.JANUARY, 17), secondEmployee.joiningDate)
        assertEquals("Employee", secondEmployee.employeeRole)
        assertEquals("Software Engineer", secondEmployee.employeeTitle)
        assertEquals("department2", secondEmployee.department)
        assertEquals("Inactive", secondEmployee.employeeStatus)
        assertEquals("Berlin", secondEmployee.location)
        assertEquals("Test User 0030", secondEmployee.superordinate)
    }

}