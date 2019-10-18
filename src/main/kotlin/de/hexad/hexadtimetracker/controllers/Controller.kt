package de.hexad.hexadtimetracker.controllers

import de.hexad.hexadtimetracker.sources.EmployeeLeavesSource
import de.hexad.hexadtimetracker.sources.EmployeesSource
import de.hexad.hexadtimetracker.sources.HolidaysSource
import de.hexad.hexadtimetracker.sources.TimesheetsSource
import de.hexad.hexadtimetracker.types.EmployeeLeaveType
import de.hexad.hexadtimetracker.types.EmployeeType
import de.hexad.hexadtimetracker.types.HolidayType
import de.hexad.hexadtimetracker.types.TimesheetType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val employeesSource: EmployeesSource,
                 private val employeeLeavesSource: EmployeeLeavesSource,
                 private val holidaysSource: HolidaysSource,
                 private val timesheetsSource: TimesheetsSource) {

    @GetMapping("/employees")
    fun getEmployees(): List<EmployeeType> {
        return employeesSource.getEmployees()
    }

    @GetMapping("/leaves/{leaveId}")
    fun getLeaves(@PathVariable leaveId: String): List<EmployeeLeaveType> {
        return employeeLeavesSource.getEmployeeLeaves(leaveId)
    }

    @GetMapping("/holidays")
    fun getLeaves(): List<HolidayType> {
        return holidaysSource.getHolidays()
    }

    @GetMapping("/timesheets")
    fun getTimesheets(): List<TimesheetType> {
        return timesheetsSource.getTimesheets()
    }
}