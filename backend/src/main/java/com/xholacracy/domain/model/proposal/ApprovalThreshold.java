package com.xholacracy.domain.model.proposal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ApprovalThreshold 值对象 - 审批阈值
 * 定义提案自动批准的条件
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalThreshold {
    
    /**
     * 最小赞成票数
     */
    @Column(name = "min_approve_votes")
    private Integer minApproveVotes;
    
    /**
     * 最大反对票数
     */
    @Column(name = "max_object_votes")
    private Integer maxObjectVotes;
    
    /**
     * 最小赞成比例（0-100）
     */
    @Column(name = "min_approve_percentage")
    private Integer minApprovePercentage;
    
    /**
     * 是否需要一致同意
     */
    @Column(name = "requires_unanimous")
    private Boolean requiresUnanimous;
    
    /**
     * 创建审批阈值
     */
    public static ApprovalThreshold create(Integer minApproveVotes, 
                                          Integer maxObjectVotes,
                                          Integer minApprovePercentage, 
                                          boolean requiresUnanimous) {
        if (minApproveVotes != null && minApproveVotes < 0) {
            throw new IllegalArgumentException("Min approve votes cannot be negative");
        }
        if (maxObjectVotes != null && maxObjectVotes < 0) {
            throw new IllegalArgumentException("Max object votes cannot be negative");
        }
        if (minApprovePercentage != null && (minApprovePercentage < 0 || minApprovePercentage > 100)) {
            throw new IllegalArgumentException("Min approve percentage must be between 0 and 100");
        }
        
        return new ApprovalThreshold(minApproveVotes, maxObjectVotes, minApprovePercentage, requiresUnanimous);
    }
    
    /**
     * 创建简单多数阈值（超过50%赞成）
     */
    public static ApprovalThreshold simpleMajority() {
        return new ApprovalThreshold(null, null, 51, false);
    }
    
    /**
     * 创建一致同意阈值
     */
    public static ApprovalThreshold unanimous() {
        return new ApprovalThreshold(null, 0, 100, true);
    }
    
    /**
     * 检查是否满足阈值
     */
    public boolean isMet(long approveCount, long objectCount, long totalVotes) {
        if (totalVotes == 0) {
            return false;
        }
        
        // 检查一致同意要求
        if (Boolean.TRUE.equals(requiresUnanimous)) {
            return objectCount == 0 && approveCount == totalVotes;
        }
        
        // 检查最大反对票数
        if (maxObjectVotes != null && objectCount > maxObjectVotes) {
            return false;
        }
        
        // 检查最小赞成票数
        if (minApproveVotes != null && approveCount < minApproveVotes) {
            return false;
        }
        
        // 检查最小赞成比例
        if (minApprovePercentage != null) {
            double approvePercentage = (approveCount * 100.0) / totalVotes;
            if (approvePercentage < minApprovePercentage) {
                return false;
            }
        }
        
        return true;
    }
}
