package com.huibai.eomp.mapper;

import com.huibai.eomp.entity.Announcements;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 公告表 Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface AnnouncementsMapper extends BaseMapper<Announcements> {
    /**
     * 查询当前用户有权查看的公告列表
     * @param deptId 用户所属部门ID，如果是管理员可以传null
     */
    List<Announcements> selectAuthorizedAnnouncements(@Param("deptId") Integer deptId);
}
