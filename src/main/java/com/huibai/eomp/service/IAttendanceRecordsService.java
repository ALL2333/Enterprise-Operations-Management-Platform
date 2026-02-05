package com.huibai.eomp.service;

import com.huibai.eomp.dto.AttendanceCheckDTO;
import com.huibai.eomp.entity.AttendanceRecords;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 考勤记录表 服务类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface IAttendanceRecordsService extends IService<AttendanceRecords> {
    /**
     * 核心打卡业务
     * @param userId 用户ID
     * @param dto 打卡参数（包含类型、坐标、备注等）
     * @return 成功或失败
     */
    boolean check(Integer userId, AttendanceCheckDTO dto);

    /**
     * 获取用户今日考勤状态概览（用于前端仪表盘显示）
     * @param userId 用户ID
     * @return 包含上班/下班打卡时间及状态的Map
     */
    Map<String, Object> getTodayStatus(Integer userId);
}
