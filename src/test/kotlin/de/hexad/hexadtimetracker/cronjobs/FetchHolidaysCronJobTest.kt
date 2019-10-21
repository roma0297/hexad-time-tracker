package de.hexad.hexadtimetracker.cronjobs

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
import org.springframework.core.convert.ConversionService


class FetchHolidaysCronJobTes {

    val holidaysSource = mockk<HolidaysSource>()
    val holidaysRepository = mockk<HolidaysRepository>(relaxed = true)
    val conversionService = mockk<ConversionService>()

    val fetchHolidaysCronJob = FetchHolidaysCronJob(
            holidaysSource = holidaysSource,
            holidaysRepository = holidaysRepository,
            conversionService = conversionService
    )

    @Test
    fun `should convert all holiday DTOs to models and store them in the DB`() {
        //given
        val holidayType1 = mockk<HolidayType>()
        val holidayType2 = mockk<HolidayType>()
        every { holidaysSource.getHolidays() } returns listOf(holidayType1, holidayType2)
        val holidayModel1 = mockk<HolidayModel>()
        val holidayModel2 = mockk<HolidayModel>()
        every { conversionService.convert(holidayType1, HolidayModel::class.java) } returns holidayModel1
        every { conversionService.convert(holidayType2, HolidayModel::class.java) } returns holidayModel2

        //when
        fetchHolidaysCronJob.perform()

        //then
        val holidaysCapturingSlot = slot<List<HolidayModel>>()
        verify(exactly = 1) { holidaysRepository.saveAll(capture(holidaysCapturingSlot)) }
        val holidays = holidaysCapturingSlot.captured
        assertEquals(2, holidays.size)
        assertTrue(holidays.containsAll(listOf(holidayModel1, holidayModel2)))
    }


}