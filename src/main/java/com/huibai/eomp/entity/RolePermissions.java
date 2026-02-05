package com.huibai.eomp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色权限关联表
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Getter
@Setter
@TableName("role_permissions")
public class RolePermissions implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roleId;

    private Integer permissionId;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
