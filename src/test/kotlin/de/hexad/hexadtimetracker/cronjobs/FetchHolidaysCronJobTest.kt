package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.converters.HolidayTypeToHolidayModelConverter
import de.hexad.hexadtimetracker.models.HolidayModel
import de.hexad.hexadtimetracker.repositories.HolidaysRepository
import de.hexad.hexadtimetracker.sources.HolidaysSource
import de.hexad.hexadtimetracker.types.HolidayType
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue


class FetchHolidaysCronJobTes {

    val holidaysSource = mockk<HolidaysSource>()
    val holidaysRepository = mockk<HolidaysRepository>(relaxed = true)
    val holidayTypeToHolidayModelConverter = mockk<HolidayTypeToHolidayModelConverter>()

    val fetchHolidaysCronJob = FetchHolidaysCronJob(
            holidaysSource = holidaysSource,
            holidaysRepository = holidaysRepository,
            holidayTypeToHolidayModelConverter = holidayTypeToHolidayModelConverter
    )

    @Test
    fun `should convert all holiday DTOs to models and store them in the DB`() {
        //given
        val holidayType1 = mockk<HolidayType>()
        val holidayType2 = mockk<HolidayType>()
        every { holidaysSource.getHolidays() } returns listOf(holidayType1, holidayType2)
        val holidayModel1 = mockk<HolidayModel>()
        val holidayModel2 = mockk<HolidayModel>()
        every { holidayTypeToHolidayModelConverter.convert(holidayType1) } returns holidayModel1
        every { holidayTypeToHolidayModelConverter.convert(holidayType2) } returns holidayModel2

        //when
        fetchHolidaysCronJob.perform()

        //then
        val holidaysCapturingSlot = slot<List<HolidayModel>>()
        verify (exactly = 1){ holidaysRepository.saveAll(capture(holidaysCapturingSlot)) }
        val holidays = holidaysCapturingSlot.captured
        assertEquals(2, holidays.size)
        assertTrue(holidays.containsAll(listOf(holidayModel1, holidayModel2)))
    }


}