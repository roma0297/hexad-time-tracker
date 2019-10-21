package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.models.EmployeeModel
import de.hexad.hexadtimetracker.repositories.EmployeeRepository
import de.hexad.hexadtimetracker.sources.EmployeesSource
import org.springframework.core.convert.ConversionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FetchEmployeeRecordsCronJob(private val employeesSource: EmployeesSource,
                                  private val employeeRepository: EmployeeRepository,
                                  private val conversionService: ConversionService) {

    @Scheduled(cron = "0 0 2 * * ?")
    fun perform() {
        employeeRepository.saveAll(employeesSource.getEmployees()
                .map { employeeType -> conversionService.convert(employeeType, EmployeeModel::class.java) })
    }
}