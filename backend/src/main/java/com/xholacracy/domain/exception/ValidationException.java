package com.xholacracy.domain.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证异常
 * 当领域对象验证失败时抛出
 */
public class ValidationException extends DomainException {
    
    private final Map<String, String> validationErrors;
    
    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
        this.validationErrors = new HashMap<>();
    }
    
    public ValidationException(String message, Map<String, String> validationErrors) {
        super("VALIDATION_ERROR", message);
        this.validationErrors = validationErrors != null ? validationErrors : new HashMap<>();
    }
    
    public ValidationException(String field, String error) {
        super("VALIDATION_ERROR", String.format("Validation failed for field '%s': %s", field, error));
        this.validationErrors = new HashMap<>();
        this.validationErrors.put(field, error);
    }
    
    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
    
    public void addError(String field, String error) {
        this.validationErrors.put(field, error);
    }
}
