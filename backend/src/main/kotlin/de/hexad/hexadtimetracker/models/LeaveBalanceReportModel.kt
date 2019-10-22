package de.hexad.hexadtimetracker.models;

import javax.persistence.*

@Entity
@Table(name = "leave_balance_reports")
class LeaveBalanceReportModel(
        @Column(name = "leave_type_id")
        val id: String,
        @Column(name = "leave_type_nme")
        val name: String,

        @Column(name = "permitted_count")
        val permittedCount: Int,
        @Column(name = "balance_count")
        val balanceCount: Double,
        @Column(name = "availed_count")
        val availedCount: Int,
        @Column(name = "unit")
        val unit: String,

        @Column(name = "is_half_day_enabled")
        val isHalfDayEnabled: Boolean,
        @Column(name = "is_quarter_day_enabled")
        val isQuarterDayEnabled: Boolean,
        @Column(name = "is_hour_enabled")
        val isHourEnabled: Boolean,

        @Column(name = "color_code")
        val colorCode: String,
        @Column(name = "is_balance_hidden")
        val isBalanceHidden: Boolean,
        @Column(name = "show_file_upload_after")
        val showFileUploadAfter: Double,
        @Column(name = "version")
        val version: Int,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "employee_pk", insertable = false, updatable = false)
        var employee: EmployeeModel? = null
) : AbstractJpaPersistable()
