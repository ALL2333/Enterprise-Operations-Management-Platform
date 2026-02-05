package com.huibai.eomp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huibai.eomp.common.result.Result;
import com.huibai.eomp.common.security.UserContext;
import com.huibai.eomp.dto.UpdatePasswordDTO;
import com.huibai.eomp.dto.UserDTO;
import com.huibai.eomp.entity.Users;
import com.huibai.eomp.service.IDepartmentsService;
import com.huibai.eomp.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@RestController
@RequestMapping("/system/users")
public class UsersController {

    @Autowired
    private IUsersService userService;

    @Autowired
    private IDepartmentsService departmentService;

    // 分页列表
    @GetMapping
    public Result<IPage<Users>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username) {

        Page<Users> page = new Page<>(pageNum, pageSize);
        IPage<Users> data = userService.findUserPageWithDept(page, username);
        return Result.success(data);
    }

    // 状态切换
    @PatchMapping("/{id}/status")
    public Result<String> changeStatus(@PathVariable Integer id, @RequestParam String status) {
        Users user = new Users();
        user.setId(id);
        user.setStatus(status);
        userService.updateById(user);
        return Result.success("状态更新成功");
    }

    // 删除
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:delete') or hasAuthority('*:*:*')")
    public Result deleteUser(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success();
    }

    @PutMapping("/profile")
    public Result<String> updateProfile(@RequestBody Users user) {
        // 仅允许修改姓名、电话、邮箱等非敏感字段
        userService.updateById(user);
        return Result.success("资料更新成功");
    }

    @PutMapping("/updatePwd")
    public Result<String> updatePwd(@RequestBody UpdatePasswordDTO dto) {
        // 1. 获取当前登录用户 ID (从 UserContext 获取)
        Integer userId = UserContext.getUserId();
        Users user = userService.getById(userId);

        // 2. 校验旧密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(dto.getOldPassword(), user.getPasswordHash())) {
            return Result.error(500, "旧密码错误");
        }

        // 3. 更新新密码
        user.setPasswordHash(encoder.encode(dto.getNewPassword()));
        userService.updateById(user);
        return Result.success("密码修改成功，请重新登录");
    }

    @GetMapping("/me")
    public Result<Map<String, Object>> getCurrentUser() {
        // 1. 从自定义上下文获取当前登录用户 ID
        Integer userId = UserContext.getUserId();

        if (userId == null) {
            return Result.error(500, "未找到登录信息");
        }

        // 2. 调用 Service 层获取包含权限的综合信息
        // 这样逻辑更聚合，方便后续在多个地方复用“获取当前人完整信息”的逻辑
        Map<String, Object> userInfo = userService.getUserInfoWithPermissions(userId);

        if (userInfo != null && userInfo.get("user") != null) {
            return Result.success(userInfo);
        }

        return Result.error(500, "用户不存在或权限加载失败");
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // 1. 获取总用户数
        long userCount = userService.count();

        // 2. 获取总部门数
        long deptCount = departmentService.count();

        // 3. 统计状态为 active 的活跃用户数
        long activeUsers = userService.count(new QueryWrapper<Users>().eq("status", "active"));

        stats.put("userCount", userCount);
        stats.put("deptCount", deptCount);
        stats.put("activeUsers", activeUsers);
        stats.put("updateTime", LocalDateTime.now());

        return Result.success(stats);
    }

    @GetMapping("/statistics/daily-growth")
    public Result<Map<String, List<?>>> getDailyGrowth() {
        List<String> days = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        // 获取最近 7 天的日期并查询数据库
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            days.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));

            // 统计当天 00:00:00 到 23:59:59 创建的用户
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);

            long count = userService.count(new QueryWrapper<Users>()
                    .between("created_at", start, end));
            counts.add(count);
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("days", days);
        result.put("counts", counts);

        return Result.success(result);
    }

    @GetMapping("/{id}/roles")
    public Result<List<Integer>> getUserRoles(@PathVariable Integer id) {
        // 这个方法需要在 userService 中实现，查询 user_roles 表返回 role_id 列表
        List<Integer> roleIds = userService.getRoleIdsByUserId(id);
        return Result.success(roleIds);
    }

    // 新增
    @PostMapping
    public Result<?> add(@RequestBody UserDTO userDTO) {
        // 调用统一的 addUser 方法
        userService.addUser(userDTO);
        return Result.success();
    }

    // 修改
    @PutMapping
    @PreAuthorize("hasAuthority('system:user:edit') or hasAuthority('*:*:*')")
    public Result updateUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return Result.success();
    }

    @GetMapping("/list/all")
    public Result<List<Users>> listAll() {
        // 这里直接查询所有用户，用于部门负责人、角色分配等下拉列表
        return Result.success(userService.list());
    }
}
