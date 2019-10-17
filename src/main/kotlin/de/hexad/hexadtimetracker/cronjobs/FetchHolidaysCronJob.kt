package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.converters.HolidayTypeToHolidayModelConverter
import de.hexad.hexadtimetracker.repositories.HolidaysRepository
import de.hexad.hexadtimetracker.sources.HolidaysSource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FetchHolidaysCronJob(private val holidaysSource: HolidaysSource,
                           private val holidaysRepository: HolidaysRepository,
                           private val holidayTypeToHolidayModelConverter: HolidayTypeToHolidayModelConverter) {

    @Scheduled(cron = "0 0 3 * * ?")
    fun perform() {
        holidaysRepository.saveAll(holidaysSource.getHolidays()
                .map { holidayTypeToHolidayModelConverter.convert(it) })
    }
}