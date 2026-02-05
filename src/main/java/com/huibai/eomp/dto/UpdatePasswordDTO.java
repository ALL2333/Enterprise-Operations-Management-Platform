package com.huibai.eomp.dto;

import lombok.Data;

/**
 * @author zhouyihao
 */
@Data
public class UpdatePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
