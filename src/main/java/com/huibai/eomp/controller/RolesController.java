package com.huibai.eomp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huibai.eomp.common.result.Result;
import com.huibai.eomp.entity.Roles;
import com.huibai.eomp.service.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@RestController
@RequestMapping("/system/roles")
public class RolesController {

    @Autowired
    private IRolesService rolesService;

    // 1. 原有的：供用户管理下拉框使用 (无需变动，保留即可)
    @GetMapping("/all")
    public Result<List<Roles>> getAllRoles() {
        return Result.success(rolesService.list());
    }

    // 2. 新增：分页查询角色（用于角色管理主页面）
    @GetMapping
    @PreAuthorize("hasAuthority('system:role:query') or hasAuthority('*:*:*')")
    public Result list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Roles role) {
        Page<Roles> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Roles> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(role.getName()), Roles::getName, role.getName());
        return Result.success(rolesService.page(page, wrapper));
    }

    // 3. 新增：保存
    @PostMapping
    @PreAuthorize("hasAuthority('system:role:add') or hasAuthority('system:role:edit') or hasAuthority('*:*:*')")
    public Result saveOrUpdate(@RequestBody Roles role) {
        rolesService.saveOrUpdate(role);
        return Result.success();
    }

    // 4. 新增：删除角色 (调用带逻辑检查的 service 方法)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:delete') or hasAuthority('*:*:*')")
    public Result remove(@PathVariable Integer id) {
        return rolesService.deleteRoleWithCheck(id);
    }

    // 修改 RolesController.java 中的 update 方法
    @PutMapping("/{id}") // 增加 {id} 占位符
    @PreAuthorize("hasAuthority('system:role:edit') or hasAuthority('*:*:*')")
    public Result update(@PathVariable Integer id, @RequestBody Roles role) {
        role.setId(id); // 确保 ID 被正确设置
        rolesService.updateById(role);
        return Result.success();
    }

    // 6. 获取该角色当前拥有的权限ID集合（用于前端回显勾选状态）
    @GetMapping("/{roleId}/permissions")
    public Result<List<Integer>> getRolePermissions(@PathVariable Integer roleId) {
        List<Integer> permissionIds = rolesService.getPermissionIdsByRoleId(roleId);
        return Result.success(permissionIds);
    }

    // 7. 提交新的权限分配方案
    @PostMapping("/{roleId}/permissions")
    public Result updatePermissions(@PathVariable Integer roleId, @RequestBody List<Integer> permissionIds) {
        rolesService.updateRolePermissions(roleId, permissionIds);
        return Result.success("权限分配成功");
    }

    // 获取所有启用状态的角色列表，用于前端下拉框选择
    @GetMapping("/list/all")
    public Result<List<Roles>> listAll() {
        return Result.success(rolesService.list());
    }
}
