package com.huibai.eomp.mapper;

import com.huibai.eomp.entity.UserRoles;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface UserRolesMapper extends BaseMapper<UserRoles> {
    @Select("SELECT role_id FROM user_roles WHERE user_id = #{userId}")
    List<Integer> selectRoleIdsByUserId(Integer userId);

    @Delete("DELETE FROM user_roles WHERE user_id = #{userId}")
    void deleteByUserId(Integer userId);
}
