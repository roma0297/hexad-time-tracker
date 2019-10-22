package de.hexad.hexadtimetracker.converters

import de.hexad.hexadtimetracker.models.TimesheetModel
import de.hexad.hexadtimetracker.types.TimesheetType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.ZoneId

@Component
class TimesheetTypeToTimesheetModelConverter : Converter<TimesheetType, TimesheetModel> {
    override fun convert(source: TimesheetType) = TimesheetModel(
            employeeId = source.employeeId,
            employeeName = source.employeeName,
            employeeEmail = source.employeeEmail,
            timesheetName = source.timesheetName,
            startDate = source.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            endDate = source.endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            totalHours = source.totalHours,
            approvedTotalHours = source.approvedTotalHours,
            billableHours = source.billableHours,
            approvedBillableHours = source.approvedBillableHours,
            approvedNonBillableHours = source.approvedNonBillableHours,
            status = source.status
    )
}