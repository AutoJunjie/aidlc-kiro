package com.xholacracy.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * 领域异常
 * 用于表示违反领域规则的情况
 */
public class DomainException extends BusinessException {
    
    public DomainException(String errorCode, String message) {
        super(errorCode, message, HttpStatus.BAD_REQUEST);
    }
    
    public DomainException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, HttpStatus.BAD_REQUEST, cause);
    }
    
    /**
     * 创建一个简单的领域异常，使用默认错误码
     */
    public static DomainException of(String message) {
        return new DomainException("DOMAIN_RULE_VIOLATION", message);
    }
}
