package de.hexad.hexadtimetracker.converters

import de.hexad.hexadtimetracker.models.ProjectModel
import de.hexad.hexadtimetracker.types.ProjectType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class ProjectTypeToProjectModelConverter : Converter<ProjectType, ProjectModel> {
    override fun convert(source: ProjectType) = ProjectModel(
            id = source.id,
            name = source.name,
            status = source.status,
            ownerId = source.ownerId,
            ownerName = source.ownerName,
            projectManagerId = source.projectManagerId,
            projectManagerName = source.projectManagerName,
            isDeleteAllowed = source.isDeleteAllowed,
            projectCost = source.projectCost
    )
}