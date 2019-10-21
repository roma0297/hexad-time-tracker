package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.models.EmployeeModel
import de.hexad.hexadtimetracker.models.LeaveBalanceReportModel
import de.hexad.hexadtimetracker.repositories.EmployeeRepository
import de.hexad.hexadtimetracker.sources.EmployeesSource
import de.hexad.hexadtimetracker.sources.LeaveBalanceReportsSource
import de.hexad.hexadtimetracker.types.EmployeeType
import de.hexad.hexadtimetracker.types.LeaveBalanceReportType
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
    @MockK
    var leaveBalanceReportsSource = mockk<LeaveBalanceReportsSource>()

    private var fetchEmployeeRecordsCronJob = FetchEmployeeRecordsCronJob(
            employeesSource = employeesSource,
            employeeRepository = employeeRepository,
            conversionService = conversionService,
            leaveBalanceReportsSource = leaveBalanceReportsSource)

    @Test
    fun `should retrieve all employee DTOs, convert them to models and store in the DB`() {
        //given
        val employeeDto1 = mockk<EmployeeType>()
        val employeeDto2 = mockk<EmployeeType>()

        every { employeesSource.getEmployees() } returns listOf(employeeDto1, employeeDto2)

        val employeeModel1 = mockk<EmployeeModel>(relaxed = true)
        every { employeeModel1.employeeStatus } returns "Active"
        every { employeeModel1.employeeId } returns "employeeId1"
        every { conversionService.convert(employeeDto1, EmployeeModel::class.java) } returns employeeModel1
        val leaveBalanceReportType1 = mockk<LeaveBalanceReportType>()
        val leaveBalanceReportModel1 = mockk<LeaveBalanceReportModel>()
        every { conversionService.convert(leaveBalanceReportType1, LeaveBalanceReportModel::class.java) } returns leaveBalanceReportModel1
        every { leaveBalanceReportsSource.getLeaveBalanceReportsForEmployee("employeeId1") } returns listOf(leaveBalanceReportType1)

        val employeeModel2 = mockk<EmployeeModel>(relaxed = true)
        every { employeeModel2.employeeStatus } returns "Active"
        every { employeeModel2.employeeId } returns "employeeId2"
        every { conversionService.convert(employeeDto2, EmployeeModel::class.java) } returns employeeModel2
        val leaveBalanceReportType2 = mockk<LeaveBalanceReportType>()
        val leaveBalanceReportModel2 = mockk<LeaveBalanceReportModel>()
        every { conversionService.convert(leaveBalanceReportType2, LeaveBalanceReportModel::class.java) } returns leaveBalanceReportModel2
        val leaveBalanceReportType3 = mockk<LeaveBalanceReportType>()
        val leaveBalanceReportModel3 = mockk<LeaveBalanceReportModel>()
        every { conversionService.convert(leaveBalanceReportType3, LeaveBalanceReportModel::class.java) } returns leaveBalanceReportModel3
        every { leaveBalanceReportsSource.getLeaveBalanceReportsForEmployee("employeeId2") } returns listOf(leaveBalanceReportType2, leaveBalanceReportType3)

        //when
        fetchEmployeeRecordsCronJob.perform()

        //then
        val employeesCapturingSlot = slot<List<EmployeeModel>>()

        verify(exactly = 1) { employeeRepository.saveAll(capture(employeesCapturingSlot)) }
        verify(exactly = 1) { employeeModel1.addLeaveBalanceReport(leaveBalanceReportModel1) }
        verify(exactly = 1) { employeeModel2.addLeaveBalanceReport(leaveBalanceReportModel2) }
        verify(exactly = 1) { employeeModel2.addLeaveBalanceReport(leaveBalanceReportModel3) }

        val employees = employeesCapturingSlot.captured
        assertEquals(2, employees.size)
        assertTrue(employees.containsAll(listOf(employeeModel1, employeeModel2)))
    }

}