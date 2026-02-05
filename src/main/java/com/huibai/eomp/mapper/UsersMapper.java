package com.huibai.eomp.mapper;

import com.huibai.eomp.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface UsersMapper extends BaseMapper<Users> {
    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM users WHERE username = #{username} LIMIT 1")
    Users selectByUsername(String username);
}
