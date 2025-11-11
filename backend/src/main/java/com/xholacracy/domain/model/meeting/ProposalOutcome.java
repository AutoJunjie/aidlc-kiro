package com.xholacracy.domain.model.meeting;

import com.xholacracy.domain.model.proposal.ProposalId;
import com.xholacracy.domain.model.proposal.ProposalStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 提案结果值对象
 * 
 * 记录会议中处理提案的结果
 */
@Embeddable
public class ProposalOutcome {
    
    @Embedded
    private ProposalId proposalId;
    
    @Enumerated(EnumType.STRING)
    private ProposalStatus finalStatus;
    
    private String outcome;
    
    private String notes;
    
    private LocalDateTime processedAt;
    
    protected ProposalOutcome() {
        // For JPA
    }
    
    private ProposalOutcome(ProposalId proposalId, ProposalStatus finalStatus, String outcome, String notes) {
        this.proposalId = Objects.requireNonNull(proposalId, "ProposalId cannot be null");
        this.finalStatus = Objects.requireNonNull(finalStatus, "FinalStatus cannot be null");
        this.outcome = outcome;
        this.notes = notes;
        this.processedAt = LocalDateTime.now();
    }
    
    /**
     * 创建提案结果
     * 
     * @param proposalId 提案ID
     * @param finalStatus 最终状态
     * @param outcome 结果描述
     * @param notes 备注
     * @return 提案结果
     */
    public static ProposalOutcome create(ProposalId proposalId, ProposalStatus finalStatus, 
                                        String outcome, String notes) {
        return new ProposalOutcome(proposalId, finalStatus, outcome, notes);
    }
    
    /**
     * 创建已批准的提案结果
     * 
     * @param proposalId 提案ID
     * @param notes 备注
     * @return 提案结果
     */
    public static ProposalOutcome approved(ProposalId proposalId, String notes) {
        return new ProposalOutcome(proposalId, ProposalStatus.APPROVED, "Approved", notes);
    }
    
    /**
     * 创建已撤回的提案结果
     * 
     * @param proposalId 提案ID
     * @param reason 撤回原因
     * @return 提案结果
     */
    public static ProposalOutcome withdrawn(ProposalId proposalId, String reason) {
        return new ProposalOutcome(proposalId, ProposalStatus.WITHDRAWN, "Withdrawn", reason);
    }
    
    /**
     * 创建延期处理的提案结果
     * 
     * @param proposalId 提案ID
     * @param reason 延期原因
     * @return 提案结果
     */
    public static ProposalOutcome deferred(ProposalId proposalId, String reason) {
        return new ProposalOutcome(proposalId, ProposalStatus.SUBMITTED, "Deferred", reason);
    }
    
    // Getters
    
    public ProposalId getProposalId() {
        return proposalId;
    }
    
    public ProposalStatus getFinalStatus() {
        return finalStatus;
    }
    
    public String getOutcome() {
        return outcome;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalOutcome that = (ProposalOutcome) o;
        return Objects.equals(proposalId, that.proposalId) &&
                finalStatus == that.finalStatus;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(proposalId, finalStatus);
    }
    
    @Override
    public String toString() {
        return "ProposalOutcome{" +
                "proposalId=" + proposalId +
                ", finalStatus=" + finalStatus +
                ", outcome='" + outcome + '\'' +
                ", processedAt=" + processedAt +
                '}';
    }
}
