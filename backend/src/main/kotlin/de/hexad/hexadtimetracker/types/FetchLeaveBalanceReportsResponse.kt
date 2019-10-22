package de.hexad.hexadtimetracker.types

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.minidev.json.JSONArray

@JsonIgnoreProperties(ignoreUnknown = true)
class FetchLeaveBalanceReportsResponse {

    lateinit var reports: List<LeaveBalanceReportType>

    companion object {
        val mapper = jacksonObjectMapper().also { it.registerModule(JavaTimeModule()) }
    }

    @JsonProperty("response")
    fun unpackNested(response: Map<String, Any>) {
        val reportsJsonArray = JSONArray()
        if (response["result"] is List<*>) {
            reportsJsonArray.addAll(response["result"] as List<*>)
            reports = mapper.readValue(reportsJsonArray.toJSONString())
        }
    }
}