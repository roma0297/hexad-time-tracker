package de.hexad.hexadtimetracker.types

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class EmployeeLeaveBalanceType(@JsonProperty(value = "Id")
                               val id: String,
                               @JsonProperty(value = "Name")
                               val name: String,

                               @JsonProperty(value = "PermittedCount")
                               val permittedCount: Int,
                               @JsonProperty(value = "BalanceCount")
                               val balanceCount: Double,
                               @JsonProperty(value = "AvailedCount")
                               val availedCount: Int,
                               @JsonProperty(value = "Unit")
                               val unit: String,

                               @JsonProperty(value = "isHalfDayEnabled")
                               val isHalfDayEnabled: Boolean,
                               @JsonProperty(value = "isQuarterDayEnabled")
                               val isQuarterDayEnabled: Boolean,
                               @JsonProperty(value = "isHourEnabled")
                               val isHourEnabled: Boolean,

                               @JsonProperty(value = "Color")
                               val colorCode: String,
                               @JsonProperty(value = "DoNotDisplayBalance")
                               val isBalanceHidden: Boolean,
                               @JsonProperty(value = "showFileUploadAfter")
                               val showFileUploadAfter: Double,
                               @JsonProperty(value = "version")
                               val version: Int
)