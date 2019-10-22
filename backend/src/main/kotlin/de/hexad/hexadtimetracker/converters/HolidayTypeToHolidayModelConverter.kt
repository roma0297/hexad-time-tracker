package de.hexad.hexadtimetracker.converters

import de.hexad.hexadtimetracker.models.HolidayModel
import de.hexad.hexadtimetracker.types.HolidayType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.ZoneId

@Component
class HolidayTypeToHolidayModelConverter : Converter<HolidayType, HolidayModel> {
    override fun convert(holidayType: HolidayType) = HolidayModel(
            holidayId = holidayType.id,
            name = holidayType.name,
            startDate = holidayType.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            endDate = holidayType.endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            isRestrictedHoliday = holidayType.isRestrictedHoliday,
            remarks = holidayType.remarks,
            locationName = holidayType.locationName
    )

}