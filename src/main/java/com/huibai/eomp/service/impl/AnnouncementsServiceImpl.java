package com.huibai.eomp.service.impl;

import com.huibai.eomp.entity.Announcements;
import com.huibai.eomp.mapper.AnnouncementsMapper;
import com.huibai.eomp.service.IAnnouncementsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 公告表 服务实现类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Service
public class AnnouncementsServiceImpl extends ServiceImpl<AnnouncementsMapper, Announcements> implements IAnnouncementsService {

    @Override
    public List<Announcements> getUserAnnouncements(Integer deptId) {
        return baseMapper.selectAuthorizedAnnouncements(deptId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publish(Announcements announcement, boolean isAdmin, Integer userDeptId, Integer currentUserId) {
        // 1. 设置发布者ID
        announcement.setPublisherId(currentUserId);

        // 【调试代码】建议添加日志查看 isAdmin 的真实值
        System.out.println("DEBUG: 当前用户ID: " + currentUserId + " | 是否管理员判定: " + isAdmin + " | 传入的Scope: " + announcement.getScope());

        // 2. 权限校验与范围自动修正
        if (!isAdmin) {
            // 非管理员：强制限定为本部门
            announcement.setScope("department");
            announcement.setDepartmentId(userDeptId);
        } else {
            // 管理员：允许发布全员或全公司
            // 如果管理员选择了全公司或全员，必须清空 departmentId，否则 SQL 查询会过滤掉
            if ("all".equals(announcement.getScope()) || "company".equals(announcement.getScope())) {
                announcement.setDepartmentId(null);
            } else if ("department".equals(announcement.getScope()) && announcement.getDepartmentId() == null) {
                // 如果管理员选了发布到本部门，但没传部门ID，自动填充
                announcement.setDepartmentId(userDeptId);
            }
        }

        return this.save(announcement);
    }

    @Override
    public Announcements getDetailAndRecordView(Integer id, Integer userId) {
        return this.getById(id);
    }
}
