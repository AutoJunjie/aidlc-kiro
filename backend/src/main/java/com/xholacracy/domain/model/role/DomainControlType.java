package com.xholacracy.domain.model.role;

/**
 * 领域控制类型枚举
 */
public enum DomainControlType {
    /**
     * 独占控制 - 角色对该领域拥有完全控制权
     */
    EXCLUSIVE,
    
    /**
     * 共享控制 - 角色与其他角色共享该领域的控制权
     */
    SHARED,
    
    /**
     * 咨询控制 - 角色在该领域有咨询权但无决策权
     */
    ADVISORY
}
