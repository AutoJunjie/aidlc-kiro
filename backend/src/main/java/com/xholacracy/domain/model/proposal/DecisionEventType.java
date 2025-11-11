package com.xholacracy.domain.model.proposal;

/**
 * 决策事件类型枚举
 * 记录提案处理过程中的各种事件
 */
public enum DecisionEventType {
    /**
     * 提案创建
     */
    PROPOSAL_CREATED,
    
    /**
     * 提案提交
     */
    PROPOSAL_SUBMITTED,
    
    /**
     * 阶段变更
     */
    STAGE_CHANGED,
    
    /**
     * 提出问题
     */
    QUESTION_ASKED,
    
    /**
     * 回答问题
     */
    QUESTION_ANSWERED,
    
    /**
     * 添加反应
     */
    REACTION_ADDED,
    
    /**
     * 提案修改
     */
    PROPOSAL_AMENDED,
    
    /**
     * 提出反对
     */
    OBJECTION_RAISED,
    
    /**
     * 反对验证
     */
    OBJECTION_VALIDATED,
    
    /**
     * 反对无效
     */
    OBJECTION_INVALIDATED,
    
    /**
     * 投票
     */
    VOTE_CAST,
    
    /**
     * 提案批准
     */
    PROPOSAL_APPROVED,
    
    /**
     * 提案应用
     */
    PROPOSAL_APPLIED,
    
    /**
     * 提案撤回
     */
    PROPOSAL_WITHDRAWN,
    
    /**
     * 提案拒绝
     */
    PROPOSAL_REJECTED
}
