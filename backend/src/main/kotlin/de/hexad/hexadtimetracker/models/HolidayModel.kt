package de.hexad.hexadtimetracker.models

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "holidays")
class HolidayModel(
        @Column(name = "holiday_id")
        val holidayId: String,
        @Column(name = "name")
        val name: String,
        @Column(name = "start_date")
        val startDate: LocalDate,
        @Column(name = "end_date")
        val endDate: LocalDate,
        @Column(name = "isRestrictedHoliday")
        val isRestrictedHoliday: Boolean,
        @Column(name = "remarks")
        val remarks: String,
        @Column(name = "location_name")
        val locationName: String
) : AbstractJpaPersistable()
