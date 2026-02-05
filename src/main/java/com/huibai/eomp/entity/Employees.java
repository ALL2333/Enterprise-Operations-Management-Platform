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
 * 员工信息表
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Getter
@Setter
@TableName("employees")
public class Employees implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("employee_number")
    private String employeeNumber;

    @TableField("gender")
    private String gender;

    @TableField("date_of_birth")
    private LocalDate dateOfBirth;

    @TableField("hire_date")
    private LocalDate hireDate;

    @TableField("position")
    private String position;

    @TableField("status")
    private String status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
