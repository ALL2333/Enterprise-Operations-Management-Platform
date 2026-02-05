package com.huibai.eomp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Getter
@Setter
@TableName("permissions")
public class Permissions implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 新增字段：权限名称
    @TableField("name")
    private String name;

    // 新增字段：父级ID
    @TableField("parent_id")
    private Integer parentId;

    // 新增字段：子级权限列表
    @TableField(exist = false)
    private List<Permissions> children;

    @TableField("code")
    private String code;

    @TableField("description")
    private String description;

    @TableField("api_endpoint")
    private String apiEndpoint;

    @TableField("frontend_component")
    private String frontendComponent;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
