package com.huibai.eomp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huibai.eomp.dto.UserDTO;
import com.huibai.eomp.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 */
public interface IUsersService extends IService<Users> {

    /**
     * 分页查询用户（关联部门名称）
     */
    IPage<Users> findUserPageWithDept(Page<Users> page, String username);

    /**
     * 新增用户及其角色关联
     * @param userDTO 包含用户信息及 roleIds
     */
    void addUser(UserDTO userDTO);

    /**
     * 更新用户及其角色关联
     * @param userDTO 包含用户信息及 roleIds
     */
    void updateUser(UserDTO userDTO);

    /**
     * 获取用户拥有的角色ID列表（用于编辑回显）
     */
    List<Integer> getRoleIdsByUserId(Integer userId);

    /**
     * 获取登录用户的综合信息（User、Roles、Permissions）
     */
    Map<String, Object> getUserInfoWithPermissions(Integer userId);

}