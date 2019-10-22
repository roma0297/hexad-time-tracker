package de.hexad.hexadtimetracker.cronjobs

import de.hexad.hexadtimetracker.models.ProjectModel
import de.hexad.hexadtimetracker.repositories.ProjectsRepository
import de.hexad.hexadtimetracker.sources.ProjectsSource
import org.springframework.core.convert.ConversionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FetchProjectsCronJob(private val projectsSource: ProjectsSource,
                           private val projectsRepository: ProjectsRepository,
                           private val conversionService: ConversionService) {

    @Scheduled(cron = "0 30 2 * * ?")
    fun perform() {
        projectsRepository.saveAll(projectsSource.getProjects()
                .map { conversionService.convert(it, ProjectModel::class.java) })
    }
}