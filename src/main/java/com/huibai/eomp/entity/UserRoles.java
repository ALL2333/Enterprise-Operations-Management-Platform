package com.huibai.eomp.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_roles")
public class UserRoles {

    private Integer userId;
    private Integer roleId;

    @TableField("created_at")
    private LocalDateTime createdAt;

    public UserRoles(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
        this.createdAt = LocalDateTime.now();
    }
}

