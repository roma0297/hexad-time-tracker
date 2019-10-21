package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.models.EmployeeModel
import de.hexad.hexadtimetracker.models.LeaveBalanceReportModel
import de.hexad.hexadtimetracker.repositories.EmployeeRepository
import de.hexad.hexadtimetracker.sources.EmployeesSource
import de.hexad.hexadtimetracker.sources.LeaveBalanceReportsSource
import org.springframework.core.convert.ConversionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FetchEmployeeRecordsCronJob(private val employeesSource: EmployeesSource,
                                  private val employeeRepository: EmployeeRepository,
                                  private val conversionService: ConversionService,
                                  private val leaveBalanceReportsSource: LeaveBalanceReportsSource) {

    @Scheduled(cron = "0 0 2 * * ?")
    fun perform() {
        val toBeSavedEmployees = employeesSource.getEmployees()
                .mapNotNull { conversionService.convert(it, EmployeeModel::class.java) }

        toBeSavedEmployees
                .filter { it.employeeStatus == "Active" }
                .forEach { employee ->
                    val receivedLeaveBalanceReports = leaveBalanceReportsSource.getLeaveBalanceReportsForEmployee(employee.employeeId)
                    val leaveBalanceReportModels = receivedLeaveBalanceReports
                            .mapNotNull { conversionService.convert(it, LeaveBalanceReportModel::class.java) }
                    leaveBalanceReportModels.forEach { employee.addLeaveBalanceReport(it) }
                }

        employeeRepository.saveAll(toBeSavedEmployees)

    }
}