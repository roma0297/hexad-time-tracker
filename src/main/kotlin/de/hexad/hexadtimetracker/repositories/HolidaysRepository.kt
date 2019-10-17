package de.hexad.hexadtimetracker.repositories

import de.hexad.hexadtimetracker.models.HolidayModel
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HolidaysRepository: CrudRepository<HolidayModel, Int>