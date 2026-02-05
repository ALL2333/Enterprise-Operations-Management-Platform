package com.huibai.eomp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huibai.eomp.common.exception.CustomException;
import com.huibai.eomp.dto.UserDTO;
import com.huibai.eomp.entity.Departments;
import com.huibai.eomp.entity.UserRoles;
import com.huibai.eomp.entity.Users;
import com.huibai.eomp.mapper.PermissionsMapper;
import com.huibai.eomp.mapper.RolesMapper;
import com.huibai.eomp.mapper.UserRolesMapper;
import com.huibai.eomp.mapper.UsersMapper;
import com.huibai.eomp.service.IDepartmentsService;
import com.huibai.eomp.service.IUsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户表 服务实现类
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IDepartmentsService departmentsService;

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Autowired
    private PermissionsMapper permissionMapper;

    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private UsersMapper usersMapper;

    /**
     * 分页查询用户（关联部门名称回显）
     */
    @Override
    public IPage<Users> findUserPageWithDept(Page<Users> page, String username) {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like(Users::getUsername, username);
        }

        IPage<Users> userPage = this.page(page, wrapper);
        List<Users> records = userPage.getRecords();

        if (!records.isEmpty()) {
            Set<Integer> deptIds = records.stream()
                    .map(Users::getDepartmentId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (!deptIds.isEmpty()) {
                Map<Integer, String> deptMap = departmentsService.listByIds(deptIds).stream()
                        .collect(Collectors.toMap(Departments::getId, Departments::getName));

                records.forEach(user -> {
                    user.setDeptName(deptMap.get(user.getDepartmentId()));
                });
            }
        }
        return userPage;
    }

    /**
     * 新增用户并保存角色关联
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserDTO dto) {
        Users user = new Users();
        BeanUtils.copyProperties(dto, user);

        // 初始密码加密，如果前端没传则设为默认密码
        String rawPassword = StringUtils.hasText(dto.getPassword()) ? dto.getPassword() : "123456";
        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        this.save(user);

        // 保存用户与角色关联
        saveUserRoleRelations(user.getId(), dto.getRoleIds());
    }

    /**
     * 更新用户及其角色关联
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO dto) {
        Users user = this.getById(dto.getId());
        if (user == null) {
            throw new CustomException("用户不存在");
        }

        // 拷贝属性，忽略用户名和密码（密码通常有单独的修改接口）
        BeanUtils.copyProperties(dto, user, "password", "username");
        this.updateById(user);

        // 先删除旧的角色关联，再插入新的
        userRolesMapper.delete(new LambdaQueryWrapper<UserRoles>().eq(UserRoles::getUserId, user.getId()));
        saveUserRoleRelations(user.getId(), dto.getRoleIds());
    }

    /**
     * 辅助方法：保存角色关联
     */
    private void saveUserRoleRelations(Integer userId, List<Integer> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Integer roleId : roleIds) {
                // 使用之前定义的双参构造器
                userRolesMapper.insert(new UserRoles(userId, roleId));
            }
        }
    }

    /**
     * 获取用户拥有的角色ID列表（用于编辑回显）
     */
    @Override
    public List<Integer> getRoleIdsByUserId(Integer userId) {
        return userRolesMapper.selectList(
                new LambdaQueryWrapper<UserRoles>().eq(UserRoles::getUserId, userId)
        ).stream().map(UserRoles::getRoleId).collect(Collectors.toList());
    }

    /**
     * 获取登录用户的综合信息（核心修复点）
     */
    @Override
    public Map<String, Object> getUserInfoWithPermissions(Integer userId) {
        Users user = this.getById(userId);

        if (user == null) {
            return null;
        }

        user.setPasswordHash(null); // 安全性处理

        // 回填部门名称
        if (user.getDepartmentId() != null) {
            Departments dept = departmentsService.getById(user.getDepartmentId());
            if (dept != null) {
                user.setDeptName(dept.getName());
            }
        }

        // 1. 获取权限字符串（例如 ["*:*:*"]）
        List<String> permissions = permissionMapper.selectCodesByUserId(userId);
        // 1. 先把数据库里可能误配的 *:*:* 剔除（防止有人在后台乱点）
        if (permissions != null) {
            permissions.removeIf(p -> "*:*:*".equals(p));
        } else {
            permissions = new ArrayList<>();
        }

        // 2. 只有真正的上帝账号 eomp_admin 才能拥有通配符
        if ("eomp_admin".equals(user.getUsername())) {
            permissions.add("*:*:*");
        }

        // 2. 获取角色名称
        List<String> roles = rolesMapper.selectRoleNamesByUserId(userId);

        // 3. 超级管理员硬编码判定，确保 eomp_admin 拥有最高权限
        if ("eomp_admin".equals(user.getUsername())) {
            if (permissions == null) {
                permissions = new ArrayList<>();
            }
            if (!permissions.contains("*:*:*")) {
                permissions.add("*:*:*");
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("permissions", permissions);
        result.put("roles", roles);
        return result;
    }
}