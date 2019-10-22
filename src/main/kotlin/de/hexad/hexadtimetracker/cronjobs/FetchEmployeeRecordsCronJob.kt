package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.models.EmployeeModel
import de.hexad.hexadtimetracker.models.LeaveBalanceReportModel
import de.hexad.hexadtimetracker.providers.UserInfo
import de.hexad.hexadtimetracker.repositories.EmployeeRepository
import de.hexad.hexadtimetracker.sources.EmployeesSource
import de.hexad.hexadtimetracker.sources.LeaveBalanceReportsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.core.convert.ConversionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class FetchEmployeeRecordsCronJob(private val employeesSource: EmployeesSource,
                                  private val employeeRepository: EmployeeRepository,
                                  private val conversionService: ConversionService,
                                  private val leaveBalanceReportsSource: LeaveBalanceReportsSource) {
    companion object {
        var I: Int = 0
    }

    @Scheduled(cron = "0 0 2 * * ?")
    fun perform() {
        val toBeSavedEmployees = employeesSource.getEmployees()
                .mapNotNull { conversionService.convert(it, EmployeeModel::class.java) }
        val token = (SecurityContextHolder.getContext().authentication.principal as UserInfo).token
        runBlocking(Dispatchers.IO) {
            toBeSavedEmployees
                    .filter { it.employeeStatus == "Active" }
                    .forEach { launch { populateWithLeaveBalanceReports(it, token) } }
        }
        employeeRepository.saveAll(toBeSavedEmployees)
    }

    suspend fun populateWithLeaveBalanceReports(employee: EmployeeModel, authenticationToken: String) {
        val receivedLeaveBalanceReports = leaveBalanceReportsSource.getLeaveBalanceReportsForEmployee(employee.employeeId, authenticationToken)
        val leaveBalanceReportModels = receivedLeaveBalanceReports
                .mapNotNull { conversionService.convert(it, LeaveBalanceReportModel::class.java) }
        leaveBalanceReportModels.forEach { employee.addLeaveBalanceReport(it) }
    }

}