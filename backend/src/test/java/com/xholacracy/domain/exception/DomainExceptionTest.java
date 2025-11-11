package com.xholacracy.domain.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

/**
 * 领域异常单元测试
 */
class DomainExceptionTest {
    
    @Test
    void shouldCreateDomainException() {
        // Given
        String errorCode = "TEST_ERROR";
        String message = "Test error message";
        
        // When
        DomainException exception = new DomainException(errorCode, message);
        
        // Then
        assertThat(exception.getErrorCode()).isEqualTo(errorCode);
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    @Test
    void shouldCreateSimpleDomainException() {
        // Given
        String message = "Simple error message";
        
        // When
        DomainException exception = DomainException.of(message);
        
        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getErrorCode()).isEqualTo("DOMAIN_RULE_VIOLATION");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    @Test
    void shouldCreateResourceNotFoundException() {
        // Given
        String resourceType = "Circle";
        String resourceId = "circle-123";
        
        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceType, resourceId);
        
        // Then
        assertThat(exception.getMessage()).contains(resourceType);
        assertThat(exception.getMessage()).contains(resourceId);
        assertThat(exception.getErrorCode()).isEqualTo("RESOURCE_NOT_FOUND");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    
    @Test
    void shouldCreatePermissionDeniedException() {
        // Given
        String operation = "delete";
        String resource = "Circle";
        
        // When
        PermissionDeniedException exception = new PermissionDeniedException(operation, resource);
        
        // Then
        assertThat(exception.getMessage()).contains(operation);
        assertThat(exception.getMessage()).contains(resource);
        assertThat(exception.getErrorCode()).isEqualTo("PERMISSION_DENIED");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    
    @Test
    void shouldCreateInvalidStateTransitionException() {
        // Given
        String currentState = "DRAFT";
        String targetState = "APPROVED";
        
        // When
        InvalidStateTransitionException exception = 
            new InvalidStateTransitionException(currentState, targetState);
        
        // Then
        assertThat(exception.getMessage()).contains(currentState);
        assertThat(exception.getMessage()).contains(targetState);
        assertThat(exception.getErrorCode()).isEqualTo("INVALID_STATE_TRANSITION");
    }
    
    @Test
    void shouldCreateValidationException() {
        // Given
        String field = "name";
        String error = "cannot be empty";
        
        // When
        ValidationException exception = new ValidationException(field, error);
        
        // Then
        assertThat(exception.getMessage()).contains(field);
        assertThat(exception.getMessage()).contains(error);
        assertThat(exception.getValidationErrors()).containsEntry(field, error);
        assertThat(exception.getErrorCode()).isEqualTo("VALIDATION_ERROR");
    }
}
