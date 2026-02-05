package com.huibai.eomp.service;

import com.huibai.eomp.common.result.Result;
import com.huibai.eomp.entity.Roles;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface IRolesService extends IService<Roles> {
    Result deleteRoleWithCheck(Integer roleId);

    /**
     * 根据角色ID获取已绑定的权限ID列表
     * @param roleId 角色ID
     * @return 权限ID集合
     */
    List<Integer> getPermissionIdsByRoleId(Integer roleId);

    /**
     * 更新角色权限关联（先删后增）
     * @param roleId 角色ID
     * @param permissionIds 权限ID集合
     */
    void updateRolePermissions(Integer roleId, List<Integer> permissionIds);
}
