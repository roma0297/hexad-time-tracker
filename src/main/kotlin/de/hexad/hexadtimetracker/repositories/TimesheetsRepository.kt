package de.hexad.hexadtimetracker.repositories

import de.hexad.hexadtimetracker.models.TimesheetModel
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TimesheetsRepository : CrudRepository<TimesheetModel, Int>