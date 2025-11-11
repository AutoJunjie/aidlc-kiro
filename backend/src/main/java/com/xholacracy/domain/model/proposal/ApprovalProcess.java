package com.xholacracy.domain.model.proposal;

import com.xholacracy.domain.model.partner.PartnerId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * ApprovalProcess 值对象 - 审批流程
 * 定义提案的审批规则和流程
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalProcess {
    
    @Embedded
    private ApprovalThreshold approvalThreshold;
    
    @ElementCollection
    @CollectionTable(name = "required_approvers", joinColumns = @JoinColumn(name = "proposal_id"))
    @Column(name = "approver_id")
    private List<String> requiredApproverIds = new ArrayList<>();
    
    @Column(name = "time_limit_hours")
    private Long timeLimitHours;
    
    /**
     * 创建审批流程
     */
    public static ApprovalProcess create(ApprovalThreshold threshold, 
                                        List<PartnerId> requiredApprovers,
                                        Duration timeLimit) {
        if (threshold == null) {
            throw new IllegalArgumentException("Approval threshold cannot be null");
        }
        
        List<String> approverIds = new ArrayList<>();
        if (requiredApprovers != null) {
            approverIds = new ArrayList<>(requiredApprovers.stream()
                .map(PartnerId::getValue)
                .toList());
        }
        
        Long timeLimitHours = timeLimit != null ? timeLimit.toHours() : null;
        
        return new ApprovalProcess(threshold, approverIds, timeLimitHours);
    }
    
    /**
     * 创建默认审批流程（简单多数）
     */
    public static ApprovalProcess defaultProcess() {
        return new ApprovalProcess(ApprovalThreshold.simpleMajority(), new ArrayList<>(), null);
    }
    
    /**
     * 检查提案是否已批准
     */
    public boolean isApproved(List<Vote> votes) {
        if (votes == null || votes.isEmpty()) {
            return false;
        }
        
        long approveCount = votes.stream()
            .filter(v -> v.getVoteType() == VoteType.APPROVE)
            .count();
        
        long objectCount = votes.stream()
            .filter(v -> v.getVoteType() == VoteType.OBJECT)
            .count();
        
        return approvalThreshold.isMet(approveCount, objectCount, votes.size());
    }
    
    /**
     * 检查是否所有必需审批者都已投票
     */
    public boolean allRequiredApproversVoted(List<Vote> votes) {
        if (requiredApproverIds.isEmpty()) {
            return true;
        }
        
        if (votes == null) {
            return false;
        }
        
        List<String> voterIds = votes.stream()
            .map(v -> v.getVoterId().getValue())
            .toList();
        
        return voterIds.containsAll(requiredApproverIds);
    }
}
