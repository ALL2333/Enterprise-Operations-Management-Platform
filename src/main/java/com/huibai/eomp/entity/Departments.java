package com.huibai.eomp.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Getter
@Setter
@TableName("departments")
public class Departments implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField(value = "parent_id", updateStrategy = FieldStrategy.IGNORED)
    private Integer parentId;

    @TableField(exist = false)
    private java.util.List<Departments> children = new java.util.ArrayList<>();

    @TableField("manager_id")
    private Integer managerId;

    @TableField(exist = false)
    private String managerName;

    @TableField("description")
    private String description;

    @TableField("status")
    private String status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
