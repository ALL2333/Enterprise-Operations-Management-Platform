package com.huibai.eomp.common.exception;

import com.huibai.eomp.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhouyihao
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获自定义业务异常 (如：余额不足、权限不够)
    @ExceptionHandler(CustomException.class)
    public Result<?> handleCustomException(CustomException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    // 捕获系统未知异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统运行异常: ", e);
        return Result.error(500, "服务器开小差了，请稍后再试");
    }
    // 专门捕获 Security 权限拒绝异常
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e) {
        // 返回明确的 403 状态码
        return Result.error(403, "权限不足，无法执行此操作");
    }
}