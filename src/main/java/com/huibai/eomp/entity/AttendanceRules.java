package com.huibai.eomp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 考勤规则表
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Getter
@Setter
@TableName("attendance_rules")
public class AttendanceRules implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("work_start_time")
    private LocalTime workStartTime;

    @TableField("work_end_time")
    private LocalTime workEndTime;

    @TableField("late_threshold")
    private Integer lateThreshold;

    @TableField("leave_early_threshold")
    private Integer leaveEarlyThreshold;

    @TableField("effective_date")
    private LocalDate effectiveDate;


    @TableField("is_active")
    private Integer isActive;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("longitude")
    private java.math.BigDecimal longitude;

    @TableField("latitude")
    private java.math.BigDecimal latitude;

    @TableField("allow_distance")
    private Integer allowDistance;
}
