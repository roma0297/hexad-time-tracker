package de.hexad.hexadtimetracker.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "projects")
class ProjectModel(
        @Column(name = "project_id")
        val id: String,
        @Column(name = "name")
        val name: String,
        @Column(name = "status")
        val status: String,
        @Column(name = "owner_id")
        val ownerId: String,
        @Column(name = "owner_name")
        val ownerName: String,
        @Column(name = "project_manager_id")
        val projectManagerId: String,
        @Column(name = "project_manager_name")
        val projectManagerName: String,
        @Column(name = "delete_allowed")
        val isDeleteAllowed: Boolean,
        @Column(name = "project_cost")
        val projectCost: Double
) : AbstractJpaPersistable()