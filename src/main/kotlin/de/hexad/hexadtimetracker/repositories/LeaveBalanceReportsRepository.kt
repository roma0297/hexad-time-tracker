package de.hexad.hexadtimetracker.repositories;

import de.hexad.hexadtimetracker.models.LeaveBalanceReportModel
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LeaveBalanceReportsRepository: CrudRepository<LeaveBalanceReportModel, Int>
