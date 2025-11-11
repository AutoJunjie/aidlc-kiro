package com.xholacracy.domain.model.meeting;

import com.xholacracy.domain.model.proposal.ProposalId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 议程项值对象
 * 
 * 表示治理会议中的一个议程项，通常关联一个提案
 */
@Embeddable
public class AgendaItem {
    
    @Embedded
    private ProposalId proposalId;
    
    private int orderIndex;
    
    @Enumerated(EnumType.STRING)
    private AgendaItemStatus status;
    
    private LocalDateTime startedAt;
    
    private LocalDateTime completedAt;
    
    private String notes;
    
    protected AgendaItem() {
        // For JPA
    }
    
    private AgendaItem(ProposalId proposalId, int orderIndex) {
        this.proposalId = Objects.requireNonNull(proposalId, "ProposalId cannot be null");
        this.orderIndex = orderIndex;
        this.status = AgendaItemStatus.PENDING;
    }
    
    /**
     * 创建议程项
     * 
     * @param proposalId 提案ID
     * @param orderIndex 排序索引
     * @return 议程项
     */
    public static AgendaItem create(ProposalId proposalId, int orderIndex) {
        return new AgendaItem(proposalId, orderIndex);
    }
    
    /**
     * 开始处理议程项
     */
    public void start() {
        if (this.status != AgendaItemStatus.PENDING) {
            throw new IllegalStateException("Only pending agenda items can be started");
        }
        this.status = AgendaItemStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
    }
    
    /**
     * 完成议程项
     * 
     * @param notes 备注
     */
    public void complete(String notes) {
        if (this.status != AgendaItemStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only in-progress agenda items can be completed");
        }
        this.status = AgendaItemStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.notes = notes;
    }
    
    /**
     * 跳过议程项
     * 
     * @param reason 跳过原因
     */
    public void skip(String reason) {
        if (this.status == AgendaItemStatus.COMPLETED) {
            throw new IllegalStateException("Cannot skip completed agenda items");
        }
        this.status = AgendaItemStatus.SKIPPED;
        this.notes = reason;
    }
    
    /**
     * 更新排序索引
     * 
     * @param newOrderIndex 新的排序索引
     */
    public void updateOrderIndex(int newOrderIndex) {
        this.orderIndex = newOrderIndex;
    }
    
    // Getters
    
    public ProposalId getProposalId() {
        return proposalId;
    }
    
    public int getOrderIndex() {
        return orderIndex;
    }
    
    public AgendaItemStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public String getNotes() {
        return notes;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgendaItem that = (AgendaItem) o;
        return orderIndex == that.orderIndex &&
                Objects.equals(proposalId, that.proposalId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(proposalId, orderIndex);
    }
    
    @Override
    public String toString() {
        return "AgendaItem{" +
                "proposalId=" + proposalId +
                ", orderIndex=" + orderIndex +
                ", status=" + status +
                '}';
    }
}
