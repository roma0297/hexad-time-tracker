package de.hexad.hexadtimetracker.controllers

import de.hexad.hexadtimetracker.sources.*
import de.hexad.hexadtimetracker.types.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val employeesSource: EmployeesSource,
                 private val employeeLeavesSource: EmployeeLeavesSource,
                 private val holidaysSource: HolidaysSource,
                 private val timesheetsSource: TimesheetsSource,
                 private val projectsSource: ProjectsSource,
                 private val leaveBalanceReportsSource: LeaveBalanceReportsSource) {

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

    @GetMapping("/projects")
    fun getProjects(): List<ProjectType> {
        return projectsSource.getProjects()
    }

    @GetMapping("/leave-balances/{employeeId}")
    fun getLeaveBalancesForEmployee(@PathVariable employeeId: String): List<LeaveBalanceReportType> {
        return leaveBalanceReportsSource.getLeaveBalanceReportsForEmployee(employeeId)
    }
}