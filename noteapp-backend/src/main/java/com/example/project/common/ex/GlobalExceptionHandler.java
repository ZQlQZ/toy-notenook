package com.example.project.common.ex;

import com.example.project.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(SQLException.class)
    public Result<String> handleSQLException(SQLException e) {
        return Result.fail(500, "数据库操作失败: " + e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.fail(500, "服务器内部错误: " + e.getMessage());
    }
}