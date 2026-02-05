package com.huibai.eomp.controller;

import com.huibai.eomp.common.result.Result;
import com.huibai.eomp.common.security.UserContext;
import com.huibai.eomp.dto.AttendanceCheckDTO;
import com.huibai.eomp.service.IAttendanceRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 考勤记录表 前端控制器
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@RestController
@RequestMapping("/attendanceRecords")
public class AttendanceRecordsController {

    @Autowired
    private IAttendanceRecordsService attendanceRecordsService;

    /**
     * 核心打卡接口
     * UC-09: 员工通过Web界面进行上下班打卡
     */
    @PostMapping("/check")
    public Result check(@RequestBody AttendanceCheckDTO dto) {
        // 通过定义的 UserContext 获取拦截器存入的当前登录用户ID
        Integer currentUserId = UserContext.getUserId();

        if (currentUserId == null) {
            return Result.error(500,"未获取到登录信息，请重新登录");
        }

        try {
            boolean success = attendanceRecordsService.check(currentUserId, dto);
            return success ? Result.success("打卡成功") : Result.error(500,"打卡失败");
        } catch (RuntimeException e) {
            return Result.error(500,"打卡异常");
        }
    }

    /**
     * 获取今日打卡状态概览
     * 用于前端仪表盘实时显示已打卡时间
     */
    @GetMapping("/today")
    public Result getTodayStatus() {
        Integer currentUserId = UserContext.getUserId();
        Map<String, Object> status = attendanceRecordsService.getTodayStatus(currentUserId);
        return Result.success(status);
    }
}