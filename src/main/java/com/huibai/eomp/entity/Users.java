package com.huibai.eomp.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Data
@TableName("users")
public class Users {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    @TableField("password_hash")
    private String passwordHash;

    @TableField("real_name")
    private String realName;

    @TableField(exist = false)
    private String deptName;

    private String email;
    private String phone;
    private String status; // 'active' or 'inactive'

    @TableField("department_id")
    private Integer departmentId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 屏蔽密码字段不返回给前端，或者使用VO对象
    // 这里为了简便，在JSON序列化时可以忽略，或者在DTO转换时处理
}
