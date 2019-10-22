package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.models.TimesheetModel
import de.hexad.hexadtimetracker.repositories.TimesheetsRepository
import de.hexad.hexadtimetracker.sources.TimesheetsSource
import org.springframework.core.convert.ConversionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FetchTimesheetsCronJob(private val timesheetsSource: TimesheetsSource,
                             private val timesheetsRepository: TimesheetsRepository,
                             private val conversionService: ConversionService) {

    @Scheduled(cron = "0 30 2 * * ?")
    fun perform() {
        timesheetsRepository.saveAll(timesheetsSource.getTimesheets()
                .map { conversionService.convert(it, TimesheetModel::class.java) })
    }
}