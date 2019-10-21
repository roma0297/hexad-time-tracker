package de.hexad.hexadtimetracker.cronjobs

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
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.core.convert.ConversionService

class FetchEmployeeRecordsCronJobTest {

    @MockK
    var employeeRepository = mockk<EmployeeRepository>(relaxed = true)
    @MockK
    var employeesSource = mockk<EmployeesSource>()
    @MockK
    var conversionService = mockk<ConversionService>()

    var fetchEmployeeRecordsCronJob = FetchEmployeeRecordsCronJob(
            employeesSource = employeesSource,
            employeeRepository = employeeRepository,
            conversionService = conversionService)

    @Test
    fun `should retrieve all employee DTOs, convert them to models and store in the DB`() {
        //given
        val employeeDto1 = mockk<EmployeeType>()
        val employeeDto2 = mockk<EmployeeType>()

        every { employeesSource.getEmployees() } returns listOf(employeeDto1, employeeDto2)

        val employeeModel1 = mockk<EmployeeModel>()
        val employeeModel2 = mockk<EmployeeModel>()
        every { conversionService.convert(employeeDto1, EmployeeModel::class.java) } returns employeeModel1
        every { conversionService.convert(employeeDto2, EmployeeModel::class.java) } returns employeeModel2

        //when
        fetchEmployeeRecordsCronJob.perform()

        //then
        val employeesCapturingSlot = slot<List<EmployeeModel>>()

        verify(exactly = 1) { employeeRepository.saveAll(capture(employeesCapturingSlot)) }

        val employees = employeesCapturingSlot.captured
        assertEquals(2, employees.size)
        assertTrue(employees.containsAll(listOf(employeeModel1, employeeModel2)))
    }

}