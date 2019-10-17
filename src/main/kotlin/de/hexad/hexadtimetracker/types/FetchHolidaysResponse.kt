package de.hexad.hexadtimetracker.types

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.minidev.json.JSONArray

@JsonIgnoreProperties(ignoreUnknown = true)
class FetchHolidaysResponse {

    lateinit var holidays: List<HolidayType>

    companion object {
        val mapper = jacksonObjectMapper().also { it.registerModule(JavaTimeModule()) }
    }

    @JsonProperty("response")
    fun unpackNested(response: Map<String, Any>) {
        val holidaysJsonArray = JSONArray()
        if (response["result"] is List<*>) {
            holidaysJsonArray.addAll(response["result"] as List<*>)
            holidays = mapper.readValue(holidaysJsonArray.toJSONString())
        }
    }
}