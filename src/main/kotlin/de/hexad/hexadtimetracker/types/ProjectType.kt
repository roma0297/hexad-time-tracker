package de.hexad.hexadtimetracker.types

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
class ProjectType(@JsonProperty(value = "projectId")
                  val id: String,
                  @JsonProperty(value = "projectName")
                  val name: String,
                  @JsonProperty(value = "projectStatus")
                  val status: String,

                  @JsonProperty(value = "ownerId")
                  val ownerId: String,
                  @JsonProperty(value = "ownerName")
                  val ownerName: String,

                  @JsonProperty(value = "projectManager")
                  val projectManagerId: String = "",
                  @JsonProperty(value = "projectManagerName")
                  val projectManagerName: String = "",

                  @JsonProperty(value = "isDeleteAllowed")
                  val isDeleteAllowed: Boolean,
                  @JsonProperty(value = "projectCost")
                  val projectCost: Double = 0.0
)