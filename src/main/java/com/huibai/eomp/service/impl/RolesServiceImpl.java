package com.huibai.eomp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huibai.eomp.common.result.Result;
import com.huibai.eomp.entity.RolePermissions;
import com.huibai.eomp.entity.Roles;
import com.huibai.eomp.entity.UserRoles;
import com.huibai.eomp.mapper.RolePermissionsMapper;
import com.huibai.eomp.mapper.RolesMapper;
import com.huibai.eomp.mapper.UserRolesMapper;
import com.huibai.eomp.service.IRolesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements IRolesService {

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Autowired
    private RolePermissionsMapper rolePermissionsMapper;

    @Override
    @Transactional
    public Result deleteRoleWithCheck(Integer roleId) {
        // 第一步：检查 user_roles 表中是否有关联记录
        QueryWrapper<UserRoles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        Long count = userRolesMapper.selectCount(queryWrapper);

        if (count > 0) {
            // 如果有关联，返回错误提示，不执行删除
            return Result.error(400, "该角色已分配给用户，禁止删除！请先在用户管理中取消关联。");
        }

        // 第二步：检查角色是否为内置管理员（可选，增加安全性）
        Roles role = this.getById(roleId);
        if ("admin".equalsIgnoreCase(role.getRoleKey())) {
            return Result.error(400, "超级管理员角色禁止删除");
        }

        // 第三步：执行物理删除角色（因为有关联的权限表 role_permissions 通常配了 ON DELETE CASCADE，所以会自动清理）
        this.removeById(roleId);
        return Result.success("删除成功");
    }

    @Override
    public List<Integer> getPermissionIdsByRoleId(Integer roleId) {
        // 查询关联表，只取出 permission_id 这一列
        List<RolePermissions> list = rolePermissionsMapper.selectList(
                new LambdaQueryWrapper<RolePermissions>().eq(RolePermissions::getRoleId, roleId)
        );
        // 转化为 List<Integer>
        return list.stream().map(RolePermissions::getPermissionId).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateRolePermissions(Integer roleId, List<Integer> permissionIds) {
        // 先物理删除该角色在 role_permissions 表中的所有旧数据
        rolePermissionsMapper.delete(new LambdaQueryWrapper<RolePermissions>()
                .eq(RolePermissions::getRoleId, roleId));

        // 批量插入勾选的新权限 ID
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Integer permiId : permissionIds) {
                RolePermissions rp = new RolePermissions();
                rp.setRoleId(roleId);
                rp.setPermissionId(permiId);
                rolePermissionsMapper.insert(rp);
            }
        }
    }
}
