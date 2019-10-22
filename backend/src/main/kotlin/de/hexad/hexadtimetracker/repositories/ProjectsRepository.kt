package de.hexad.hexadtimetracker.repositories

import de.hexad.hexadtimetracker.models.ProjectModel
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectsRepository: CrudRepository<ProjectModel, Int>