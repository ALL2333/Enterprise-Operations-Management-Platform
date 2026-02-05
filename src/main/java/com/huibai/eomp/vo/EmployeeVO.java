package com.huibai.eomp.vo;

import lombok.Data;

import java.time.LocalDate;

// EmployeeVO.java - 用于列表展示
@Data
public class EmployeeVO {
    private Integer id;
    private Integer userId;
    private String username;    // 来自 users 表
    private String realName;    // 来自 users 表
    private Integer departmentId; // 关联 departments 获取
    private String deptName;    // 关联 departments 获取
    private String employeeNumber;
    private String jobTitle;
    private String status;
    private LocalDate hireDate;
    private String position;
    private String phone;
    private String email;
}