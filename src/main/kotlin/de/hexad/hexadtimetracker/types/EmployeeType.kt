package de.hexad.hexadtimetracker.types

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class EmployeeType(@JsonProperty(value = "EmployeeID") val employeeId: String,
                   @JsonProperty(value = "First Name") val firstName: String,
                   @JsonProperty(value = "Last Name") val lastName: String,
                   @JsonProperty(value = "Department") val department: String,
                   @JsonProperty(value = "Location") val workLocation: String)