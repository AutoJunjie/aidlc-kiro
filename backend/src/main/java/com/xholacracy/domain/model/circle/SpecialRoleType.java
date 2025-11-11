package com.xholacracy.domain.model.circle;

/**
 * 特殊角色类型枚举
 * 每个圈子都有四个特殊角色
 */
public enum SpecialRoleType {
    /**
     * 圈引导 - 负责引导和协调圈子运作，负责圈的总体目标和资源分配
     */
    CIRCLE_LEAD("Circle Lead", "Guides and coordinates the circle's operations"),
    
    /**
     * 协调员 - 主持治理过程和运营实践，确保会议和流程遵循Holacracy规则
     */
    FACILITATOR("Facilitator", "Facilitates governance processes and operational practices"),
    
    /**
     * 秘书 - 维护圈的记录和会议，负责文档管理和流程解释
     */
    SECRETARY("Secretary", "Maintains circle records and meetings"),
    
    /**
     * 圈代表 - 将圈的需求在上级圈子中表达
     */
    CIRCLE_REP("Circle Rep", "Represents circle needs in parent circle");
    
    private final String displayName;
    private final String description;
    
    SpecialRoleType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}
