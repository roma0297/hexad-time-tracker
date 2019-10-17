package de.hexad.hexadtimetracker.types

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
class EmployeeLeaveType(@JsonProperty(value = "TeamEmailID") val email: String,
                        @JsonProperty(value = "Leavetype") val leaveType: String,

                        @JsonProperty(value = "From")
                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
                        val from: LocalDate,

                        @JsonProperty(value = "To")
                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
                        val to: LocalDate,

                        @JsonProperty(value = "Employee_ID.ID") val leaveId: String)