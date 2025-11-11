package com.xholacracy.domain.model.proposal;

/**
 * 提案状态枚举
 * 表示提案在其生命周期中的各个阶段
 */
public enum ProposalStatus {
    /**
     * 草稿 - 提案正在编辑中
     */
    DRAFT,
    
    /**
     * 已提交 - 提案已提交等待处理
     */
    SUBMITTED,
    
    /**
     * 提案阶段 - 集成决策流程第一阶段：提案人陈述提案
     */
    PROPOSAL_STAGE,
    
    /**
     * 澄清阶段 - 集成决策流程第二阶段：参与者提出澄清问题
     */
    CLARIFICATION_STAGE,
    
    /**
     * 反应阶段 - 集成决策流程第三阶段：参与者表达反应
     */
    REACTION_STAGE,
    
    /**
     * 修改阶段 - 集成决策流程第四阶段：提案人修改提案
     */
    AMEND_STAGE,
    
    /**
     * 反对阶段 - 集成决策流程第五阶段：参与者提出反对
     */
    OBJECTION_STAGE,
    
    /**
     * 集成阶段 - 集成决策流程第六阶段：解决反对意见
     */
    INTEGRATION_STAGE,
    
    /**
     * 已批准 - 提案通过，等待应用
     */
    APPROVED,
    
    /**
     * 已应用 - 提案已应用到组织结构
     */
    APPLIED,
    
    /**
     * 已撤回 - 提案人撤回提案
     */
    WITHDRAWN,
    
    /**
     * 已拒绝 - 提案被拒绝
     */
    REJECTED
}
