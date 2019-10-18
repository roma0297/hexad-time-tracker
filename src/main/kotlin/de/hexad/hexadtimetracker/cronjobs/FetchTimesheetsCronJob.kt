package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.converters.TimesheetTypeToTimesheetModelConverter
import de.hexad.hexadtimetracker.repositories.TimesheetsRepository
import de.hexad.hexadtimetracker.sources.TimesheetsSource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FetchTimesheetsCronJob(private val timesheetsSource: TimesheetsSource,
                             private val timesheetsRepository: TimesheetsRepository,
                             private val timesheetTypeToTimesheetModelConverter: TimesheetTypeToTimesheetModelConverter) {

    @Scheduled(cron = "0 30 2 * * ?")
    fun perform() {
        timesheetsRepository.saveAll(timesheetsSource.getTimesheets()
                .map { timesheetType -> timesheetTypeToTimesheetModelConverter.convert(timesheetType) })
    }
}