package de.hexad.hexadtimetracker.models;

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "employees")
class EmployeeModel(
        @Column(name = "employee_id")
        val employeeId: String,
        @Column(name = "record_id")
        val recordId: String,

        @Column(name = "first_name")
        val firstName: String,
        @Column(name = "last_name")
        val lastName: String,
        @Column(name = "email")
        val email: String,
        @Column(name = "photo_url")
        val photoUrl: String,
        @Column(name = "photo_download_url")
        val photoDownloadUrl: String,

        @Column(name = "last_modification_time")
        val lastModificationTime: LocalDateTime,
        @Column(name = "creation_time")
        val creationTime: LocalDateTime,
        @Column(name = "joining_date")
        val joiningDate: LocalDate,

        @Column(name = "employee_role")
        val employeeRole: String,
        @Column(name = "employee_title")
        val employeeTitle: String,
        @Column(name = "department")
        val department: String,
        @Column(name = "employee_status")
        val employeeStatus: String,
        @Column(name = "location")
        val location: String,
        @Column(name = "superordinate")
        val superordinate: String,

        @OneToMany(mappedBy = "employee", cascade = [CascadeType.ALL])
        var leaveBalanceReports: MutableList<LeaveBalanceReportModel> = mutableListOf()
) : AbstractJpaPersistable() {
    fun addLeaveBalanceReport(leaveBalanceReport: LeaveBalanceReportModel) {
        leaveBalanceReports.add(leaveBalanceReport)
        leaveBalanceReport.employee = this
    }

    fun removeLeaveBalanceReport(leaveBalanceReport: LeaveBalanceReportModel) {
            leaveBalanceReports.remove(leaveBalanceReport)
            leaveBalanceReport.employee = null
    }
}
