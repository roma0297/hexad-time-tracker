package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.converters.TimesheetTypeToTimesheetModelConverter
import de.hexad.hexadtimetracker.models.TimesheetModel
import de.hexad.hexadtimetracker.repositories.TimesheetsRepository
import de.hexad.hexadtimetracker.sources.TimesheetsSource
import de.hexad.hexadtimetracker.types.TimesheetType
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class FetchTimesheetsCronJobTest {
    private val timesheetsSource = mockk<TimesheetsSource>()
    private val timesheetsRepository = mockk<TimesheetsRepository>(relaxed = true)
    private val timesheetTypeToTimesheetModelConverter = mockk<TimesheetTypeToTimesheetModelConverter>()

    private val fetchTimesheetsCronJob = FetchTimesheetsCronJob(
            timesheetsSource = timesheetsSource,
            timesheetsRepository = timesheetsRepository,
            timesheetTypeToTimesheetModelConverter = timesheetTypeToTimesheetModelConverter
    )

    @Test
    fun `should convert all timesheet DTOs to models and store them in the DB`() {
        //given
        val timesheetType1 = mockk<TimesheetType>()
        val timesheetType2 = mockk<TimesheetType>()
        every { timesheetsSource.getTimesheets() } returns listOf(timesheetType1, timesheetType2)
        val timesheetModel1 = mockk<TimesheetModel>()
        val timesheetModel2 = mockk<TimesheetModel>()
        every { timesheetTypeToTimesheetModelConverter.convert(timesheetType1) } returns timesheetModel1
        every { timesheetTypeToTimesheetModelConverter.convert(timesheetType2) } returns timesheetModel2

        //when
        fetchTimesheetsCronJob.perform()

        //then
        val timesheetsCapturingSlot = slot<List<TimesheetModel>>()
        verify (exactly = 1){ timesheetsRepository.saveAll(capture(timesheetsCapturingSlot)) }
        val savedTimesheets = timesheetsCapturingSlot.captured
        assertEquals(2, savedTimesheets.size)
        assertTrue(savedTimesheets.containsAll(listOf(timesheetModel1, timesheetModel2)))
    }
}