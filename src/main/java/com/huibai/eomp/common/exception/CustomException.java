package com.huibai.eomp.common.exception;

import lombok.Getter;

/**
 * @author zhouyihao
 */
@Getter
public class CustomException extends RuntimeException {
    private Integer code;

    public CustomException(String message) {
        super(message);
        this.code = 400; // 默认业务异常状态码
    }

    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}