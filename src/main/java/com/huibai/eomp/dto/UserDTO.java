package com.huibai.eomp.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zhouyihao
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password; // 仅新增时使用
    private String realName;
    private String email;
    private String phone;
    private Integer departmentId;
    private String status;
    private List<Integer> roleIds; // 接收前端选中的角色ID数组
}
