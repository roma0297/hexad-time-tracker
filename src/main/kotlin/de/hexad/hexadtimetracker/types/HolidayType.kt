package de.hexad.hexadtimetracker.types

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class HolidayType(
        @JsonProperty("Id")
        val id: String,
        @JsonProperty("Name")
        val name: String,
        @JsonProperty(value = "fromDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val fromDate: Date,
        @JsonProperty(value = "toDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val toDate: Date,
        @JsonProperty("isRestrictedHoliday")
        val isRestrictedHoliday: Boolean,
        @JsonProperty("Remarks")
        val remarks: String,
        @JsonProperty("LocationName")
        val locationName: String
)