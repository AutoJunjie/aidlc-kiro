package com.xholacracy.domain.model.meeting;

import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.ProposalId;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 会议记录值对象
 * 
 * 记录治理会议的详细信息，包括参与者、签到备注、提案结果等
 */
@Embeddable
public class MeetingRecord {
    
    @Column(name = "check_in_notes", length = 2000)
    private String checkInNotes;
    
    @Column(name = "additional_notes", length = 5000)
    private String additionalNotes;
    
    @Column(name = "closing_notes", length = 2000)
    private String closingNotes;
    
    @ElementCollection
    @CollectionTable(name = "meeting_attendance", joinColumns = @JoinColumn(name = "meeting_id"))
    private List<PartnerId> attendees = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "meeting_proposal_outcomes", joinColumns = @JoinColumn(name = "meeting_id"))
    private List<ProposalOutcome> proposalOutcomes = new ArrayList<>();
    
    protected MeetingRecord() {
        // For JPA
    }
    
    private MeetingRecord(String checkInNotes, String additionalNotes, String closingNotes,
                         List<PartnerId> attendees, List<ProposalOutcome> proposalOutcomes) {
        this.checkInNotes = checkInNotes;
        this.additionalNotes = additionalNotes;
        this.closingNotes = closingNotes;
        this.attendees = new ArrayList<>(attendees);
        this.proposalOutcomes = new ArrayList<>(proposalOutcomes);
    }
    
    /**
     * 创建空的会议记录
     * 
     * @return 会议记录
     */
    public static MeetingRecord create() {
        return new MeetingRecord("", "", "", new ArrayList<>(), new ArrayList<>());
    }
    
    /**
     * 记录签到备注
     * 
     * @param notes 签到备注
     */
    public void recordCheckIn(String notes) {
        this.checkInNotes = notes;
    }
    
    /**
     * 添加额外备注
     * 
     * @param notes 额外备注
     */
    public void addAdditionalNotes(String notes) {
        if (this.additionalNotes == null || this.additionalNotes.isEmpty()) {
            this.additionalNotes = notes;
        } else {
            this.additionalNotes += "\n" + notes;
        }
    }
    
    /**
     * 记录结束轮备注
     * 
     * @param notes 结束轮备注
     */
    public void recordClosing(String notes) {
        this.closingNotes = notes;
    }
    
    /**
     * 记录参与者出席
     * 
     * @param partnerId 伙伴ID
     */
    public void recordAttendance(PartnerId partnerId) {
        Objects.requireNonNull(partnerId, "PartnerId cannot be null");
        if (!attendees.contains(partnerId)) {
            attendees.add(partnerId);
        }
    }
    
    /**
     * 批量记录参与者出席
     * 
     * @param partnerIds 伙伴ID列表
     */
    public void recordAttendance(List<PartnerId> partnerIds) {
        Objects.requireNonNull(partnerIds, "PartnerIds cannot be null");
        for (PartnerId partnerId : partnerIds) {
            recordAttendance(partnerId);
        }
    }
    
    /**
     * 记录提案处理结果
     * 
     * @param outcome 提案结果
     */
    public void recordProposalOutcome(ProposalOutcome outcome) {
        Objects.requireNonNull(outcome, "ProposalOutcome cannot be null");
        
        // 移除同一提案的旧结果（如果存在）
        proposalOutcomes.removeIf(o -> o.getProposalId().equals(outcome.getProposalId()));
        
        proposalOutcomes.add(outcome);
    }
    
    /**
     * 获取提案的处理结果
     * 
     * @param proposalId 提案ID
     * @return 提案结果（如果存在）
     */
    public ProposalOutcome getProposalOutcome(ProposalId proposalId) {
        return proposalOutcomes.stream()
                .filter(o -> o.getProposalId().equals(proposalId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 生成会议摘要
     * 
     * @return 会议摘要
     */
    public String generateSummary() {
        StringBuilder summary = new StringBuilder();
        
        summary.append("=== 会议摘要 ===\n\n");
        
        // 参与者
        summary.append("参与者: ").append(attendees.size()).append(" 人\n\n");
        
        // 签到备注
        if (checkInNotes != null && !checkInNotes.isEmpty()) {
            summary.append("签到轮:\n").append(checkInNotes).append("\n\n");
        }
        
        // 提案处理结果
        if (!proposalOutcomes.isEmpty()) {
            summary.append("处理的提案:\n");
            Map<String, Integer> outcomeStats = new HashMap<>();
            for (ProposalOutcome outcome : proposalOutcomes) {
                String status = outcome.getOutcome();
                outcomeStats.put(status, outcomeStats.getOrDefault(status, 0) + 1);
                summary.append("- ").append(outcome.getProposalId().getValue())
                       .append(": ").append(status).append("\n");
            }
            summary.append("\n统计: ");
            outcomeStats.forEach((status, count) -> 
                summary.append(status).append("(").append(count).append(") "));
            summary.append("\n\n");
        }
        
        // 额外备注
        if (additionalNotes != null && !additionalNotes.isEmpty()) {
            summary.append("备注:\n").append(additionalNotes).append("\n\n");
        }
        
        // 结束轮
        if (closingNotes != null && !closingNotes.isEmpty()) {
            summary.append("结束轮:\n").append(closingNotes).append("\n");
        }
        
        return summary.toString();
    }
    
    /**
     * 检查伙伴是否出席
     * 
     * @param partnerId 伙伴ID
     * @return 如果出席则返回true
     */
    public boolean isAttended(PartnerId partnerId) {
        return attendees.contains(partnerId);
    }
    
    /**
     * 获取出席人数
     * 
     * @return 出席人数
     */
    public int getAttendeeCount() {
        return attendees.size();
    }
    
    /**
     * 获取处理的提案数量
     * 
     * @return 提案数量
     */
    public int getProposalOutcomeCount() {
        return proposalOutcomes.size();
    }
    
    // Getters
    
    public String getCheckInNotes() {
        return checkInNotes;
    }
    
    public String getAdditionalNotes() {
        return additionalNotes;
    }
    
    public String getClosingNotes() {
        return closingNotes;
    }
    
    public List<PartnerId> getAttendees() {
        return Collections.unmodifiableList(attendees);
    }
    
    public List<ProposalOutcome> getProposalOutcomes() {
        return Collections.unmodifiableList(proposalOutcomes);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingRecord that = (MeetingRecord) o;
        return Objects.equals(attendees, that.attendees) &&
                Objects.equals(proposalOutcomes, that.proposalOutcomes);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(attendees, proposalOutcomes);
    }
    
    @Override
    public String toString() {
        return "MeetingRecord{" +
                "attendeeCount=" + attendees.size() +
                ", proposalOutcomeCount=" + proposalOutcomes.size() +
                '}';
    }
}
