package de.hexad.hexadtimetracker.converters

import de.hexad.hexadtimetracker.models.LeaveBalanceReportModel
import de.hexad.hexadtimetracker.types.LeaveBalanceReportType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class LeaveBalanceReportTypeToEmployeeLeaveBalanceReportModelConverter : Converter<LeaveBalanceReportType, LeaveBalanceReportModel> {
    override fun convert(source: LeaveBalanceReportType) = LeaveBalanceReportModel(
            id = source.id,
            name = source.name,
            permittedCount = source.permittedCount,
            balanceCount = source.balanceCount,
            availedCount = source.availedCount,
            unit = source.unit,
            isHalfDayEnabled = source.isHalfDayEnabled,
            isQuarterDayEnabled = source.isQuarterDayEnabled,
            isHourEnabled = source.isHourEnabled,
            colorCode = source.colorCode,
            isBalanceHidden = source.isBalanceHidden,
            showFileUploadAfter = source.showFileUploadAfter,
            version = source.version
    )

}