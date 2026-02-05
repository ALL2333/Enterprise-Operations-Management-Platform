package com.huibai.eomp.mapper;

import com.huibai.eomp.entity.Roles;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface RolesMapper extends BaseMapper<Roles> {
    @Select("SELECT r.name " +
            "FROM roles r " +
            "JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> selectRoleNamesByUserId(Integer userId);
}
