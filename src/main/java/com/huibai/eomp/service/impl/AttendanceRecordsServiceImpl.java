package com.huibai.eomp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huibai.eomp.dto.AttendanceCheckDTO;
import com.huibai.eomp.entity.AttendanceRecords;
import com.huibai.eomp.entity.AttendanceRules;
import com.huibai.eomp.mapper.AttendanceRecordsMapper;
import com.huibai.eomp.service.IAttendanceRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huibai.eomp.service.IAttendanceRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 考勤记录表 服务实现类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Service
public class AttendanceRecordsServiceImpl extends ServiceImpl<AttendanceRecordsMapper, AttendanceRecords> implements IAttendanceRecordsService {

    @Autowired
    private IAttendanceRulesService rulesService;

    @Override
    public boolean check(Integer userId, AttendanceCheckDTO dto) {
        // 1. 获取当前生效的规则
        AttendanceRules rule = rulesService.getOne(new LambdaQueryWrapper<AttendanceRules>()
                .eq(AttendanceRules::getIsActive, 1)
                .le(AttendanceRules::getEffectiveDate, LocalDate.now())
                .orderByDesc(AttendanceRules::getEffectiveDate)
                .last("LIMIT 1"));

        if (rule == null) {
            throw new RuntimeException("打卡失败：未配置有效的考勤规则");
        }

        // 2. 地理围栏校验 (核心功能)
        if (rule.getLatitude() != null && rule.getLongitude() != null) {
            if (dto.getLatitude() == null || dto.getLongitude() == null) {
                throw new RuntimeException("打卡失败：无法获取您的当前位置点");
            }

            double distance = calculateDistance(
                    dto.getLatitude().doubleValue(), dto.getLongitude().doubleValue(),
                    rule.getLatitude().doubleValue(), rule.getLongitude().doubleValue()
            );

            int allowed = rule.getAllowDistance() != null ? rule.getAllowDistance() : 500;
            if (distance > allowed) {
                throw new RuntimeException(String.format("不在考勤范围！当前距公司 %.0f 米，限制范围 %d 米", distance, allowed));
            }
        }

        // 3. 原有逻辑：查询今日记录
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalTime nowTime = nowDateTime.toLocalTime();
        LocalDate today = LocalDate.now();

        AttendanceRecords record = this.getOne(new LambdaQueryWrapper<AttendanceRecords>()
                .eq(AttendanceRecords::getUserId, userId)
                .eq(AttendanceRecords::getWorkDate, today));

        if (record == null) {
            record = new AttendanceRecords();
            record.setUserId(userId);
            record.setWorkDate(today);
            record.setIsException(0);
        }

        // 4. 执行状态判定
        if ("IN".equals(dto.getType())) {
            if (record.getClockInTime() != null) throw new RuntimeException("今日已完成上班打卡");
            record.setClockInTime(nowDateTime);
            if (nowTime.isAfter(rule.getWorkStartTime().plusMinutes(rule.getLateThreshold()))) {
                record.setClockInStatus("LATE");
                record.setIsException(1);
            } else {
                record.setClockInStatus("NORMAL");
            }
        } else if ("OUT".equals(dto.getType())) {
            record.setClockOutTime(nowDateTime);
            if (nowTime.isBefore(rule.getWorkEndTime().minusMinutes(rule.getLeaveEarlyThreshold()))) {
                record.setClockOutStatus("EARLY");
                record.setIsException(1);
            } else {
                record.setClockOutStatus("NORMAL");
            }
        }

        // 存储位置文本信息
        record.setLatLng(dto.getLatitude() + "," + dto.getLongitude());
        if (dto.getRemark() != null) {
            record.setRemark(dto.getRemark());
        }

        return this.saveOrUpdate(record);
    }

    /**
     * 哈弗辛公式：计算经纬度两点距离（米）
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // 地球半径
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public Map<String, Object> getTodayStatus(Integer userId) {
        AttendanceRecords record = this.getOne(new LambdaQueryWrapper<AttendanceRecords>()
                .eq(AttendanceRecords::getUserId, userId)
                .eq(AttendanceRecords::getWorkDate, LocalDate.now()));
        Map<String, Object> result = new HashMap<>();
        if (record != null) {
            result.put("clockInTime", record.getClockInTime());
            result.put("clockOutTime", record.getClockOutTime());
            result.put("clockInStatus", record.getClockInStatus());
            result.put("clockOutStatus", record.getClockOutStatus());
            result.put("isException", record.getIsException());
            result.put("remark", record.getRemark());
        } else {
            result.put("clockInStatus", "ABSENT");
            result.put("clockOutStatus", "ABSENT");
            result.put("isException", 0);
        }
        return result;
    }
}

