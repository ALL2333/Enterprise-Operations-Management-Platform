package com.huibai.eomp.mapper;

import com.huibai.eomp.entity.Permissions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface PermissionsMapper extends BaseMapper<Permissions> {
    @Select("SELECT DISTINCT p.code " +
            "FROM permissions p " +
            "JOIN role_permissions rp ON p.id = rp.permission_id " +
            "JOIN user_roles ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> selectMenuPermsByUserId(Integer userId);


    @Select("SELECT DISTINCT p.code " +
            "FROM permissions p " +
            "JOIN role_permissions rp ON p.id = rp.permission_id " +
            "JOIN user_roles ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> selectCodesByUserId(Integer userId);
}
