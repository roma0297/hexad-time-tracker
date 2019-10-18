package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.converters.ProjectTypeToProjectModelConverter
import de.hexad.hexadtimetracker.repositories.ProjectsRepository
import de.hexad.hexadtimetracker.sources.ProjectsSource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FetchProjectsCronJob(private val projectsSource: ProjectsSource,
                           private val projectsRepository: ProjectsRepository,
                           private val projectTypeToProjectModelConverter: ProjectTypeToProjectModelConverter) {

    @Scheduled(cron = "0 30 2 * * ?")
    fun perform() {
        projectsRepository.saveAll(projectsSource.getProjects()
                .map { projectType -> projectTypeToProjectModelConverter.convert(projectType) })
    }
}