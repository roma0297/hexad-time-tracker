package de.hexad.hexadtimetracker.controllers

import de.hexad.hexadtimetracker.sources.EmployeeLeavesSource
import de.hexad.hexadtimetracker.sources.EmployeesSource
import de.hexad.hexadtimetracker.sources.HolidaysSource
import de.hexad.hexadtimetracker.types.EmployeeLeaveType
import de.hexad.hexadtimetracker.types.EmployeeType
import de.hexad.hexadtimetracker.types.HolidayType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val employeesSource: EmployeesSource,
                 private val employeeLeavesSource: EmployeeLeavesSource,
                 private val holidaysSource: HolidaysSource) {

    @GetMapping("/employees")
    fun getEmployees(): List<EmployeeType> {
        return employeesSource.getEmployees()
    }

    @GetMapping("/leaves/{employeeId}")
    fun getLeaves(@PathVariable employeeId: String): List<EmployeeLeaveType> {
        return employeeLeavesSource.getEmployeeLeaves(employeeId)
    }

    @GetMapping("/holidays")
    fun getLeaves(): List<HolidayType> {
        return holidaysSource.getHolidays()
    }
}