package com.huibai.eomp.controller;

import com.huibai.eomp.common.result.Result;
import com.huibai.eomp.common.security.UserContext;
import com.huibai.eomp.entity.Announcements;
import com.huibai.eomp.entity.Users;
import com.huibai.eomp.service.IAnnouncementsService;
import com.huibai.eomp.service.IUsersService;
import com.huibai.eomp.service.impl.AnnouncementsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 公告表 前端控制器
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@RestController
@RequestMapping("/system/announcements")
public class AnnouncementsController {

    @Autowired
    private IAnnouncementsService announcementsService;
    @Autowired
    private IUsersService userService;

    @GetMapping("/list")
    public Result list() {
        Integer userId = UserContext.getUserId();
        Users user = userService.getById(userId);
        Integer deptId = (user != null) ? user.getDepartmentId() : null;
        return Result.success(announcementsService.getUserAnnouncements(deptId));
    }

    /**
     * 发布公告：后端直接判定身份
     */
    @PreAuthorize("hasAuthority('system:announcement:add')")
    @PostMapping("/add")
    public Result add(@RequestBody Announcements notice) {
        Integer userId = UserContext.getUserId();
        Users user = userService.getById(userId);

        boolean isAdmin = "eomp_admin".equals(user.getUsername());

        boolean success = announcementsService.publish(notice, isAdmin, user.getDepartmentId(), userId);
        return success ? Result.success("发布成功") : Result.error(500, "发布失败");
    }

    /**
     * 删除公告
     */
    @PreAuthorize("hasAuthority('system:announcement:delete')")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean success = announcementsService.removeById(id);
        return success ? Result.success("删除成功") : Result.error(500, "删除失败");
    }
}
