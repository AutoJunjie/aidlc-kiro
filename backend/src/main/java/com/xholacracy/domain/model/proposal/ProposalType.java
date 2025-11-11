package com.xholacracy.domain.model.proposal;

/**
 * 提案类型枚举
 */
public enum ProposalType {
    /**
     * 角色修改 - 创建、更新或删除角色
     */
    ROLE_MODIFICATION,
    
    /**
     * 政策调整 - 修改圈子政策
     */
    POLICY_ADJUSTMENT,
    
    /**
     * 圈子结构变更 - 创建、更新或删除圈子
     */
    CIRCLE_STRUCTURE_CHANGE,
    
    /**
     * 流程优化 - 改进工作流程或实践
     */
    PROCESS_OPTIMIZATION
}
