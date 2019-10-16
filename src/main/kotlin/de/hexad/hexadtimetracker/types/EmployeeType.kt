package de.hexad.hexadtimetracker.types

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*


@JsonIgnoreProperties(ignoreUnknown = true)
class EmployeeType(@JsonProperty(value = "EmployeeID") val employeeId: String,
                   @JsonProperty(value = "recordId") val recordId: String,
                   @JsonProperty(value = "First Name") val firstName: String,
                   @JsonProperty(value = "Last Name") val lastName: String,
                   @JsonProperty(value = "Department") val department: String,
                   @JsonProperty(value = "Location") val workLocation: String,
                   @JsonProperty(value = "Email ID") val email: String,
                   @JsonProperty(value = "Photo") val photoUrl: String,
                   @JsonProperty(value = "Photo_downloadUrl") val photoDownloadUrl: String,

                   @JsonProperty(value = "Modified time")
                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss")
                   val modifiedTime: Date,

                   @JsonProperty(value = "Added time")
                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MMM-dd hh:mm:ss")
                   val creationTime: Date,

                   @JsonProperty(value = "Date of joining")
                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MMM-dd")
                   val joiningDate: Date,

                   @JsonProperty(value = "Employee Role") val employeeRole: String,
                   @JsonProperty(value = "Title") val title: String,
                   @JsonProperty(value = "Employee Status") val status: String,
                   @JsonProperty(value = "Reporting To") val superordinate: String
)