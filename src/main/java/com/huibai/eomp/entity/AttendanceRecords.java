package com.huibai.eomp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 考勤记录表
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Getter
@Setter
@TableName("attendance_records")
public class AttendanceRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("work_date")
    private LocalDate workDate;

    @TableField("clock_in_time")
    private LocalDateTime clockInTime;

    @TableField("clock_out_time")
    private LocalDateTime clockOutTime;

    @TableField("clock_in_status")
    private String clockInStatus;

    @TableField("clock_out_status")
    private String clockOutStatus;

    @TableField("is_exception")
    private Integer isException;

    @TableField("lat_lng")
    private String latLng;

    @TableField("remark")
    private String remark;
}
