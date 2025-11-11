package com.xholacracy.domain.model.meeting;

import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.ProposalId;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 治理会议聚合根
 * 
 * 治理会议是 Holacracy 中用于修改圈子结构、角色、政策和选举的正式会议。
 * 会议遵循特定的流程：签到轮、议程构建、处理议程项、结束轮。
 */
@Entity
@Table(name = "governance_meetings")
public class GovernanceMeeting {
    
    @EmbeddedId
    private MeetingId id;
    
    @Embedded
    private CircleId circleId;
    
    private LocalDateTime scheduledDate;
    
    private Duration duration;
    
    @Enumerated(EnumType.STRING)
    private MeetingStatus status;
    
    @Embedded
    private PartnerId facilitatorId;
    
    @Embedded
    private PartnerId secretaryId;
    
    @Embedded
    private MeetingAgenda agenda;
    
    @ElementCollection
    @CollectionTable(name = "meeting_participants", joinColumns = @JoinColumn(name = "meeting_id"))
    private List<PartnerId> participants = new ArrayList<>();
    
    @Embedded
    private MeetingRecord meetingRecord;
    
    private LocalDateTime actualStartTime;
    
    private LocalDateTime actualEndTime;
    
    protected GovernanceMeeting() {
        // For JPA
    }
    
    private GovernanceMeeting(MeetingId id, CircleId circleId, LocalDateTime scheduledDate, Duration duration) {
        this.id = Objects.requireNonNull(id, "MeetingId cannot be null");
        this.circleId = Objects.requireNonNull(circleId, "CircleId cannot be null");
        this.scheduledDate = Objects.requireNonNull(scheduledDate, "ScheduledDate cannot be null");
        this.duration = Objects.requireNonNull(duration, "Duration cannot be null");
        this.status = MeetingStatus.SCHEDULED;
        this.agenda = MeetingAgenda.create();
        this.meetingRecord = MeetingRecord.create();
    }
    
    /**
     * 创建治理会议
     * 
     * @param circleId 圈子ID
     * @param scheduledDate 计划日期时间
     * @param duration 会议时长
     * @return 治理会议
     */
    public static GovernanceMeeting create(CircleId circleId, LocalDateTime scheduledDate, Duration duration) {
        MeetingId id = MeetingId.generate();
        return new GovernanceMeeting(id, circleId, scheduledDate, duration);
    }
    
    /**
     * 设置协调员
     * 
     * @param facilitatorId 协调员ID
     */
    public void setFacilitator(PartnerId facilitatorId) {
        Objects.requireNonNull(facilitatorId, "FacilitatorId cannot be null");
        this.facilitatorId = facilitatorId;
    }
    
    /**
     * 设置秘书
     * 
     * @param secretaryId 秘书ID
     */
    public void setSecretary(PartnerId secretaryId) {
        Objects.requireNonNull(secretaryId, "SecretaryId cannot be null");
        this.secretaryId = secretaryId;
    }
    
    /**
     * 添加议程项
     * 
     * @param proposalId 提案ID
     */
    public void addAgendaItem(ProposalId proposalId) {
        if (this.status != MeetingStatus.SCHEDULED && this.status != MeetingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot add agenda items to " + this.status + " meetings");
        }
        this.agenda.addItem(proposalId);
    }
    
    /**
     * 移除议程项
     * 
     * @param proposalId 提案ID
     */
    public void removeAgendaItem(ProposalId proposalId) {
        if (this.status != MeetingStatus.SCHEDULED) {
            throw new IllegalStateException("Cannot remove agenda items from " + this.status + " meetings");
        }
        this.agenda.removeItem(proposalId);
    }
    
    /**
     * 重新排序议程
     * 
     * @param proposalIds 按新顺序排列的提案ID列表
     */
    public void reorderAgenda(List<ProposalId> proposalIds) {
        if (this.status != MeetingStatus.SCHEDULED && this.status != MeetingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot reorder agenda for " + this.status + " meetings");
        }
        this.agenda.reorder(proposalIds);
    }
    
    /**
     * 添加参与者
     * 
     * @param partnerId 伙伴ID
     */
    public void addParticipant(PartnerId partnerId) {
        Objects.requireNonNull(partnerId, "PartnerId cannot be null");
        if (!this.participants.contains(partnerId)) {
            this.participants.add(partnerId);
        }
    }
    
    /**
     * 批量添加参与者
     * 
     * @param partnerIds 伙伴ID列表
     */
    public void addParticipants(List<PartnerId> partnerIds) {
        Objects.requireNonNull(partnerIds, "PartnerIds cannot be null");
        for (PartnerId partnerId : partnerIds) {
            addParticipant(partnerId);
        }
    }
    
    /**
     * 移除参与者
     * 
     * @param partnerId 伙伴ID
     */
    public void removeParticipant(PartnerId partnerId) {
        if (this.status != MeetingStatus.SCHEDULED) {
            throw new IllegalStateException("Cannot remove participants from " + this.status + " meetings");
        }
        this.participants.remove(partnerId);
    }
    
    /**
     * 开始会议
     */
    public void start() {
        if (this.status != MeetingStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled meetings can be started");
        }
        this.status = MeetingStatus.IN_PROGRESS;
        this.actualStartTime = LocalDateTime.now();
    }
    
    /**
     * 结束会议
     */
    public void end() {
        if (this.status != MeetingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only in-progress meetings can be ended");
        }
        this.status = MeetingStatus.COMPLETED;
        this.actualEndTime = LocalDateTime.now();
    }
    
    /**
     * 取消会议
     */
    public void cancel() {
        if (this.status == MeetingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed meetings");
        }
        this.status = MeetingStatus.CANCELLED;
    }
    
    /**
     * 开始处理议程项
     * 
     * @param proposalId 提案ID
     */
    public void startAgendaItem(ProposalId proposalId) {
        if (this.status != MeetingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only process agenda items during in-progress meetings");
        }
        this.agenda.startItem(proposalId);
    }
    
    /**
     * 完成议程项
     * 
     * @param proposalId 提案ID
     * @param notes 备注
     */
    public void completeAgendaItem(ProposalId proposalId, String notes) {
        if (this.status != MeetingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete agenda items during in-progress meetings");
        }
        this.agenda.completeItem(proposalId, notes);
    }
    
    /**
     * 跳过议程项
     * 
     * @param proposalId 提案ID
     * @param reason 跳过原因
     */
    public void skipAgendaItem(ProposalId proposalId, String reason) {
        if (this.status != MeetingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only skip agenda items during in-progress meetings");
        }
        this.agenda.skipItem(proposalId, reason);
    }
    
    /**
     * 记录提案处理结果
     * 
     * @param outcome 提案结果
     */
    public void recordProposalOutcome(ProposalOutcome outcome) {
        Objects.requireNonNull(outcome, "ProposalOutcome cannot be null");
        if (this.status != MeetingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only record outcomes during in-progress meetings");
        }
        this.meetingRecord.recordProposalOutcome(outcome);
    }
    
    /**
     * 记录参与者出席
     * 
     * @param partnerId 伙伴ID
     */
    public void recordAttendance(PartnerId partnerId) {
        Objects.requireNonNull(partnerId, "PartnerId cannot be null");
        this.meetingRecord.recordAttendance(partnerId);
    }
    
    /**
     * 批量记录参与者出席
     * 
     * @param partnerIds 伙伴ID列表
     */
    public void recordAttendance(List<PartnerId> partnerIds) {
        Objects.requireNonNull(partnerIds, "PartnerIds cannot be null");
        this.meetingRecord.recordAttendance(partnerIds);
    }
    
    /**
     * 记录签到备注
     * 
     * @param notes 签到备注
     */
    public void recordCheckIn(String notes) {
        if (this.status != MeetingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only record check-in during in-progress meetings");
        }
        this.meetingRecord.recordCheckIn(notes);
    }
    
    /**
     * 添加会议备注
     * 
     * @param notes 备注
     */
    public void addNotes(String notes) {
        if (this.status != MeetingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only add notes during in-progress meetings");
        }
        this.meetingRecord.addAdditionalNotes(notes);
    }
    
    /**
     * 记录结束轮备注
     * 
     * @param notes 结束轮备注
     */
    public void recordClosing(String notes) {
        if (this.status != MeetingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only record closing during in-progress meetings");
        }
        this.meetingRecord.recordClosing(notes);
    }
    
    /**
     * 更新会议时间
     * 
     * @param scheduledDate 新的计划日期时间
     */
    public void reschedule(LocalDateTime scheduledDate) {
        if (this.status != MeetingStatus.SCHEDULED) {
            throw new IllegalStateException("Can only reschedule scheduled meetings");
        }
        this.scheduledDate = Objects.requireNonNull(scheduledDate, "ScheduledDate cannot be null");
    }
    
    /**
     * 更新会议时长
     * 
     * @param duration 新的会议时长
     */
    public void updateDuration(Duration duration) {
        if (this.status != MeetingStatus.SCHEDULED) {
            throw new IllegalStateException("Can only update duration for scheduled meetings");
        }
        this.duration = Objects.requireNonNull(duration, "Duration cannot be null");
    }
    
    /**
     * 检查会议是否已过期（计划时间已过但未开始）
     * 
     * @return 如果已过期则返回true
     */
    public boolean isOverdue() {
        return this.status == MeetingStatus.SCHEDULED && 
               LocalDateTime.now().isAfter(this.scheduledDate.plus(this.duration));
    }
    
    /**
     * 获取会议实际时长
     * 
     * @return 实际时长（如果会议已结束）
     */
    public Duration getActualDuration() {
        if (this.actualStartTime != null && this.actualEndTime != null) {
            return Duration.between(this.actualStartTime, this.actualEndTime);
        }
        return null;
    }
    
    /**
     * 检查伙伴是否是参与者
     * 
     * @param partnerId 伙伴ID
     * @return 如果是参与者则返回true
     */
    public boolean isParticipant(PartnerId partnerId) {
        return this.participants.contains(partnerId);
    }
    
    /**
     * 生成会议摘要
     * 
     * @return 会议摘要
     */
    public String generateSummary() {
        return this.meetingRecord.generateSummary();
    }
    
    // Getters
    
    public MeetingId getId() {
        return id;
    }
    
    public CircleId getCircleId() {
        return circleId;
    }
    
    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }
    
    public Duration getDuration() {
        return duration;
    }
    
    public MeetingStatus getStatus() {
        return status;
    }
    
    public PartnerId getFacilitatorId() {
        return facilitatorId;
    }
    
    public PartnerId getSecretaryId() {
        return secretaryId;
    }
    
    public MeetingAgenda getAgenda() {
        return agenda;
    }
    
    public List<PartnerId> getParticipants() {
        return Collections.unmodifiableList(participants);
    }
    
    public MeetingRecord getMeetingRecord() {
        return meetingRecord;
    }
    
    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }
    
    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GovernanceMeeting that = (GovernanceMeeting) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "GovernanceMeeting{" +
                "id=" + id +
                ", circleId=" + circleId +
                ", scheduledDate=" + scheduledDate +
                ", status=" + status +
                ", agendaItemCount=" + agenda.size() +
                ", participantCount=" + participants.size() +
                '}';
    }
}
