package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.models.HolidayModel
import de.hexad.hexadtimetracker.repositories.HolidaysRepository
import de.hexad.hexadtimetracker.sources.HolidaysSource
import org.springframework.core.convert.ConversionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FetchHolidaysCronJob(private val holidaysSource: HolidaysSource,
                           private val holidaysRepository: HolidaysRepository,
                           private val conversionService: ConversionService) {

    @Scheduled(cron = "0 0 3 * * ?")
    fun perform() {
        holidaysRepository.saveAll(holidaysSource.getHolidays()
                .map { conversionService.convert(it, HolidayModel::class.java) })
    }
}