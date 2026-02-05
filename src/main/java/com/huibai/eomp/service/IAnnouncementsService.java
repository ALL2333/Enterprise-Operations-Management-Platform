package com.huibai.eomp.service;

import com.huibai.eomp.entity.Announcements;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 公告表 服务类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface IAnnouncementsService extends IService<Announcements> {
    /**
     * UC-20: 根据用户权限获取可见的公告列表
     */
    List<Announcements> getUserAnnouncements(Integer deptId);

    /**
     * UC-19: 发布公告 (内部包含权限校验逻辑)
     * @param announcement 公告实体
     * @param isAdmin 是否为超级管理员
     * @param userDeptId 当前操作人的部门ID
     * @param currentUserId 当前操作人的用户ID (修复数据库 NOT NULL 报错)
     * @return 是否成功
     */
    boolean publish(Announcements announcement, boolean isAdmin, Integer userDeptId, Integer currentUserId);

    /**
     * 查看公告详情
     */
    Announcements getDetailAndRecordView(Integer id, Integer userId);
}
