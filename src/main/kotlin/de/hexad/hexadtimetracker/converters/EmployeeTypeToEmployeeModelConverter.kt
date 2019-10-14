package de.hexad.hexadtimetracker.converters

import de.hexad.hexadtimetracker.models.EmployeeModel
import de.hexad.hexadtimetracker.types.EmployeeType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.ZoneId

@Component
class EmployeeTypeToEmployeeModelConverter : Converter<EmployeeType, EmployeeModel> {
    override fun convert(employeeType: EmployeeType): EmployeeModel {

        return EmployeeModel(
                employeeId = employeeType.employeeId,
                recordId = employeeType.recordId,
                firstName = employeeType.firstName,
                lastName = employeeType.lastName,
                email = employeeType.email,
                photoUrl = employeeType.photoUrl,
                photoDownloadUrl = employeeType.photoDownloadUrl,
                lastModificationTime = employeeType.modifiedTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                creationTime = employeeType.creationTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                joiningDate = employeeType.joiningDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                employeeRole = employeeType.employeeRole,
                employeeTitle = employeeType.title,
                department = employeeType.department,
                employeeStatus = employeeType.status,
                location = employeeType.workLocation,
                superordinate = employeeType.superordinate
        )
    }
}