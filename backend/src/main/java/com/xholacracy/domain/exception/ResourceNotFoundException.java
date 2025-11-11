package com.xholacracy.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * 资源未找到异常
 * 当请求的资源不存在时抛出
 */
public class ResourceNotFoundException extends BusinessException {
    
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(
            "RESOURCE_NOT_FOUND",
            String.format("%s with id '%s' not found", resourceType, resourceId),
            HttpStatus.NOT_FOUND
        );
    }
    
    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }
}
