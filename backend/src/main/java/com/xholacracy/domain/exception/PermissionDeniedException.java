package com.xholacracy.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * 权限拒绝异常
 * 当用户没有权限执行某个操作时抛出
 */
public class PermissionDeniedException extends BusinessException {
    
    public PermissionDeniedException(String message) {
        super("PERMISSION_DENIED", message, HttpStatus.FORBIDDEN);
    }
    
    public PermissionDeniedException(String operation, String resource) {
        super(
            "PERMISSION_DENIED",
            String.format("Permission denied to perform '%s' on '%s'", operation, resource),
            HttpStatus.FORBIDDEN
        );
    }
}
