package com.xholacracy.domain.model.meeting;

import com.xholacracy.domain.model.proposal.ProposalId;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 会议议程值对象
 * 
 * 管理治理会议的议程项列表
 */
@Embeddable
public class MeetingAgenda {
    
    @ElementCollection
    @CollectionTable(name = "meeting_agenda_items", joinColumns = @JoinColumn(name = "meeting_id"))
    @OrderBy("orderIndex ASC")
    private List<AgendaItem> items = new ArrayList<>();
    
    protected MeetingAgenda() {
        // For JPA
    }
    
    private MeetingAgenda(List<AgendaItem> items) {
        this.items = new ArrayList<>(items);
    }
    
    /**
     * 创建空议程
     * 
     * @return 会议议程
     */
    public static MeetingAgenda create() {
        return new MeetingAgenda(new ArrayList<>());
    }
    
    /**
     * 添加议程项
     * 
     * @param proposalId 提案ID
     */
    public void addItem(ProposalId proposalId) {
        Objects.requireNonNull(proposalId, "ProposalId cannot be null");
        
        // 检查是否已存在
        if (items.stream().anyMatch(item -> item.getProposalId().equals(proposalId))) {
            throw new IllegalArgumentException("Proposal already in agenda");
        }
        
        int nextOrderIndex = items.size();
        AgendaItem item = AgendaItem.create(proposalId, nextOrderIndex);
        items.add(item);
    }
    
    /**
     * 移除议程项
     * 
     * @param proposalId 提案ID
     */
    public void removeItem(ProposalId proposalId) {
        Objects.requireNonNull(proposalId, "ProposalId cannot be null");
        
        boolean removed = items.removeIf(item -> item.getProposalId().equals(proposalId));
        if (!removed) {
            throw new IllegalArgumentException("Proposal not found in agenda");
        }
        
        // 重新排序
        reorderItems();
    }
    
    /**
     * 重新排序议程项
     * 
     * @param proposalIds 按新顺序排列的提案ID列表
     */
    public void reorder(List<ProposalId> proposalIds) {
        Objects.requireNonNull(proposalIds, "ProposalIds cannot be null");
        
        if (proposalIds.size() != items.size()) {
            throw new IllegalArgumentException("ProposalIds size must match agenda items size");
        }
        
        List<AgendaItem> reorderedItems = new ArrayList<>();
        for (int i = 0; i < proposalIds.size(); i++) {
            ProposalId proposalId = proposalIds.get(i);
            AgendaItem item = findItemByProposalId(proposalId)
                    .orElseThrow(() -> new IllegalArgumentException("Proposal not found in agenda: " + proposalId));
            item.updateOrderIndex(i);
            reorderedItems.add(item);
        }
        
        this.items = reorderedItems;
    }
    
    /**
     * 开始处理议程项
     * 
     * @param proposalId 提案ID
     */
    public void startItem(ProposalId proposalId) {
        AgendaItem item = findItemByProposalId(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("Proposal not found in agenda"));
        item.start();
    }
    
    /**
     * 完成议程项
     * 
     * @param proposalId 提案ID
     * @param notes 备注
     */
    public void completeItem(ProposalId proposalId, String notes) {
        AgendaItem item = findItemByProposalId(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("Proposal not found in agenda"));
        item.complete(notes);
    }
    
    /**
     * 跳过议程项
     * 
     * @param proposalId 提案ID
     * @param reason 跳过原因
     */
    public void skipItem(ProposalId proposalId, String reason) {
        AgendaItem item = findItemByProposalId(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("Proposal not found in agenda"));
        item.skip(reason);
    }
    
    /**
     * 获取当前进行中的议程项
     * 
     * @return 当前议程项（如果有）
     */
    public Optional<AgendaItem> getCurrentItem() {
        return items.stream()
                .filter(item -> item.getStatus() == AgendaItemStatus.IN_PROGRESS)
                .findFirst();
    }
    
    /**
     * 获取下一个待处理的议程项
     * 
     * @return 下一个议程项（如果有）
     */
    public Optional<AgendaItem> getNextPendingItem() {
        return items.stream()
                .filter(item -> item.getStatus() == AgendaItemStatus.PENDING)
                .findFirst();
    }
    
    /**
     * 检查是否所有议程项都已完成
     * 
     * @return 如果所有议程项都已完成则返回true
     */
    public boolean isAllCompleted() {
        return !items.isEmpty() && items.stream()
                .allMatch(item -> item.getStatus() == AgendaItemStatus.COMPLETED || 
                                 item.getStatus() == AgendaItemStatus.SKIPPED);
    }
    
    /**
     * 获取议程项数量
     * 
     * @return 议程项数量
     */
    public int size() {
        return items.size();
    }
    
    /**
     * 检查议程是否为空
     * 
     * @return 如果议程为空则返回true
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    /**
     * 获取所有议程项（不可修改）
     * 
     * @return 议程项列表
     */
    public List<AgendaItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    private Optional<AgendaItem> findItemByProposalId(ProposalId proposalId) {
        return items.stream()
                .filter(item -> item.getProposalId().equals(proposalId))
                .findFirst();
    }
    
    private void reorderItems() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).updateOrderIndex(i);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingAgenda that = (MeetingAgenda) o;
        return Objects.equals(items, that.items);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
    
    @Override
    public String toString() {
        return "MeetingAgenda{" +
                "itemCount=" + items.size() +
                '}';
    }
}
