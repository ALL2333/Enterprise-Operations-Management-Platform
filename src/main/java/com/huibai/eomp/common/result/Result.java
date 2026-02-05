package com.huibai.eomp.common.result;

import lombok.Data;

/**
 * @author zhouyihao
 */
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    // 1. 现有的有参成功方法
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    // 2. 新增的无参成功方法（解决你现在的报错）
    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}