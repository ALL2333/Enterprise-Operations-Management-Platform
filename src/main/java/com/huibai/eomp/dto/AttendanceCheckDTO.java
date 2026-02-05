package com.huibai.eomp.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 考勤打卡请求对象 - 增强版
 * @author zhouyihao
 */
@Data
public class AttendanceCheckDTO {
    /**
     * 打卡类型: "IN"-上班, "OUT"-下班
     */
    private String type;

    /**
     * 经度 (Longitude)
     */
    private BigDecimal longitude;

    /**
     * 纬度 (Latitude)
     */
    private BigDecimal latitude;

    /**
     * 打卡地点名称 (前端反向解析地理位置后传入，如：xx大厦A座)
     */
    private String addressName;

    /**
     * 设备唯一标识 (防止多人用一个手机代打卡)
     */
    private String deviceId;

    /**
     * 打卡备注 (如：外勤打卡原因说明)
     */
    private String remark;

    /**
     * 现场照片URL (高级功能：外勤时强制拍照)
     */
    private String photoUrl;

    /**
     * 客户端IP (后端辅助校验)
     */
    private String clientIp;
}