package de.hexad.hexadtimetracker.models

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "timesheets")
class TimesheetModel(
        @Column(name = "employee_id")
        val employeeId: String,
        @Column(name = "employee_name")
        val employeeName: String,
        @Column(name = "employee_email")
        val employeeEmail: String,

        @Column(name = "timesheet_name")
        val timesheetName: String,
        @Column(name = "start_date")
        val startDate: LocalDate,
        @Column(name = "end_date")
        val endDate: LocalDate,

        @Column(name = "total_hours")
        val totalHours: String,
        @Column(name = "approved_total_hours")
        val approvedTotalHours: String,
        @Column(name = "billable_hours")
        val billableHours: String,
        @Column(name = "approved_billable_hours")
        val approvedBillableHours: String,
        @Column(name = "approved_non_billable_hours")
        val approvedNonBillableHours: String,
        @Column(name = "status")
        val status: String) : AbstractJpaPersistable()
