package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.converters.EmployeeTypeToEmployeeModelConverter
import de.hexad.hexadtimetracker.repositories.EmployeeRepository
import de.hexad.hexadtimetracker.sources.EmployeesSource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FetchEmployeeRecordsCronJob(private val employeesSource: EmployeesSource,
                                  private val employeeRepository: EmployeeRepository,
                                  private val employeeTypeToEmployeeModelConverter: EmployeeTypeToEmployeeModelConverter) {

    @Scheduled(cron = "0 0 2 * * ?")
    fun perform() {
        employeeRepository.saveAll(employeesSource.getEmployees()
                .map { employeeType -> employeeTypeToEmployeeModelConverter.convert(employeeType) })
    }
}