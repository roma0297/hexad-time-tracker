package de.hexad.hexadtimetracker.repositories;

import de.hexad.hexadtimetracker.models.EmployeeModel
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository: CrudRepository<EmployeeModel, Int>
