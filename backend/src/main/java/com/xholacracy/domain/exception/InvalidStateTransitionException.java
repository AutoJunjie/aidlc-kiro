package com.xholacracy.domain.exception;

/**
 * 无效状态转换异常
 * 当尝试进行不允许的状态转换时抛出
 */
public class InvalidStateTransitionException extends DomainException {
    
    public InvalidStateTransitionException(String currentState, String targetState) {
        super(
            "INVALID_STATE_TRANSITION",
            String.format("Cannot transition from '%s' to '%s'", currentState, targetState)
        );
    }
    
    public InvalidStateTransitionException(String currentState, String targetState, String reason) {
        super(
            "INVALID_STATE_TRANSITION",
            String.format("Cannot transition from '%s' to '%s': %s", currentState, targetState, reason)
        );
    }
}
