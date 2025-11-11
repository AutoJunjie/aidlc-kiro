package com.xholacracy.domain.model.proposal;

/**
 * 投票类型枚举
 */
public enum VoteType {
    /**
     * 赞成 - 同意提案
     */
    APPROVE,
    
    /**
     * 反对 - 对提案有反对意见
     */
    OBJECT,
    
    /**
     * 弃权 - 不表达意见
     */
    ABSTAIN
}
