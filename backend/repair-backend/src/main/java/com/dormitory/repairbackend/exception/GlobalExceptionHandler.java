package com.dormitory.repairbackend.exception;

import com.dormitory.repairbackend.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获我们自己定义的业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.error(e.getMessage());
    }

    // 捕获所有未知的系统异常（兜底）
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace(); // 打印到控制台方便你自己排查
        return Result.error("系统内部错误，请联系管理员");
    }
}