package de.hexad.hexadtimetracker.types

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class TimesheetType(@JsonProperty("employeeId")
                    val employeeId: String,
                    @JsonProperty("employeeName")
                    val employeeName: String,
                    @JsonProperty("employeeEmail")
                    val employeeEmail: String,

                    @JsonProperty("timesheetName")
                    val timesheetName: String,
                    @JsonProperty("toDate")
                    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
                    val startDate: Date,
                    @JsonProperty("fromDate")
                    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
                    val endDate: Date,

                    @JsonProperty("totalHours")
                    val totalHours: String,
                    @JsonProperty("approvedTotalHours")
                    val approvedTotalHours: String,
                    @JsonProperty("billableHours")
                    val billableHours: String,
                    @JsonProperty("approvedBillableHours")
                    val approvedBillableHours: String,
                    @JsonProperty("approvedNonBillableHours")
                    val approvedNonBillableHours: String,
                    @JsonProperty("status")
                    val status: String)