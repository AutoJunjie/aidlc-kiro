package com.xholacracy.domain.model.proposal;

import com.xholacracy.domain.exception.InvalidStateTransitionException;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Proposal 聚合根 - 提案
 * 表示解决紧张的建议变更
 */
@Entity
@Table(name = "proposals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Proposal {
    
    @EmbeddedId
    private ProposalId id;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Embedded
    private Tension tension;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "proposal_type", nullable = false)
    private ProposalType proposalType;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "circle_id"))
    })
    private CircleId circleId;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "proposer_id"))
    })
    private PartnerId proposerId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProposalStatus status;
    
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    
    @Column(name = "submitted_date")
    private LocalDateTime submittedDate;
    
    @Embedded
    private ApprovalProcess approvalProcess;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "proposal_id")
    @OrderBy("timestamp ASC")
    private List<DecisionEvent> decisionHistory = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "proposal_id")
    private List<Question> questions = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "proposal_id")
    @OrderBy("orderIndex ASC")
    private List<Reaction> reactions = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "proposal_id")
    private List<Amendment> amendments = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "proposal_id")
    private List<Objection> objections = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "proposal_id")
    private List<Vote> votes = new ArrayList<>();
    
    /**
     * 创建提案（工厂方法）
     */
    public static Proposal create(String title, Tension tension, ProposalType type, 
                                  CircleId circleId, PartnerId proposerId) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Proposal title cannot be null or empty");
        }
        if (tension == null) {
            throw new IllegalArgumentException("Tension cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Proposal type cannot be null");
        }
        if (circleId == null) {
            throw new IllegalArgumentException("Circle ID cannot be null");
        }
        if (proposerId == null) {
            throw new IllegalArgumentException("Proposer ID cannot be null");
        }
        
        Proposal proposal = new Proposal();
        proposal.id = ProposalId.generate();
        proposal.title = title;
        proposal.tension = tension;
        proposal.proposalType = type;
        proposal.circleId = circleId;
        proposal.proposerId = proposerId;
        proposal.status = ProposalStatus.DRAFT;
        proposal.createdDate = LocalDateTime.now();
        proposal.approvalProcess = ApprovalProcess.defaultProcess();
        proposal.addDecisionEvent(DecisionEventType.PROPOSAL_CREATED, proposerId, "Proposal created");
        
        return proposal;
    }
    
    /**
     * 提交提案
     */
    public void submit() {
        if (this.status != ProposalStatus.DRAFT) {
            throw new InvalidStateTransitionException(
                this.status.name(), 
                ProposalStatus.SUBMITTED.name(),
                "Only draft proposals can be submitted"
            );
        }
        this.status = ProposalStatus.SUBMITTED;
        this.submittedDate = LocalDateTime.now();
        this.addDecisionEvent(DecisionEventType.PROPOSAL_SUBMITTED, this.proposerId, "Proposal submitted");
    }
    
    /**
     * 开始提案阶段
     */
    public void startProposalStage() {
        if (this.status != ProposalStatus.SUBMITTED) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                ProposalStatus.PROPOSAL_STAGE.name(),
                "Only submitted proposals can enter proposal stage"
            );
        }
        this.status = ProposalStatus.PROPOSAL_STAGE;
        this.addDecisionEvent(DecisionEventType.STAGE_CHANGED, null, "Entered Proposal Stage");
    }
    
    /**
     * 进入澄清阶段
     */
    public void moveToClarificationStage() {
        if (this.status != ProposalStatus.PROPOSAL_STAGE) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                ProposalStatus.CLARIFICATION_STAGE.name(),
                "Can only move to clarification stage from proposal stage"
            );
        }
        this.status = ProposalStatus.CLARIFICATION_STAGE;
        this.addDecisionEvent(DecisionEventType.STAGE_CHANGED, null, "Entered Clarification Stage");
    }
    
    /**
     * 进入反应阶段
     */
    public void moveToReactionStage() {
        if (this.status != ProposalStatus.CLARIFICATION_STAGE) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                ProposalStatus.REACTION_STAGE.name(),
                "Can only move to reaction stage from clarification stage"
            );
        }
        this.status = ProposalStatus.REACTION_STAGE;
        this.addDecisionEvent(DecisionEventType.STAGE_CHANGED, null, "Entered Reaction Stage");
    }
    
    /**
     * 进入修改阶段
     */
    public void moveToAmendStage() {
        if (this.status != ProposalStatus.REACTION_STAGE) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                ProposalStatus.AMEND_STAGE.name(),
                "Can only move to amend stage from reaction stage"
            );
        }
        this.status = ProposalStatus.AMEND_STAGE;
        this.addDecisionEvent(DecisionEventType.STAGE_CHANGED, null, "Entered Amend Stage");
    }
    
    /**
     * 进入反对阶段
     */
    public void moveToObjectionStage() {
        if (this.status != ProposalStatus.AMEND_STAGE && this.status != ProposalStatus.INTEGRATION_STAGE) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                ProposalStatus.OBJECTION_STAGE.name(),
                "Can only move to objection stage from amend or integration stage"
            );
        }
        this.status = ProposalStatus.OBJECTION_STAGE;
        this.addDecisionEvent(DecisionEventType.STAGE_CHANGED, null, "Entered Objection Stage");
    }
    
    /**
     * 进入集成阶段
     */
    public void moveToIntegrationStage() {
        if (this.status != ProposalStatus.OBJECTION_STAGE) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                ProposalStatus.INTEGRATION_STAGE.name(),
                "Can only move to integration stage from objection stage"
            );
        }
        this.status = ProposalStatus.INTEGRATION_STAGE;
        this.addDecisionEvent(DecisionEventType.STAGE_CHANGED, null, "Entered Integration Stage");
    }
    
    /**
     * 批准提案
     */
    public void approve() {
        if (this.status != ProposalStatus.OBJECTION_STAGE) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                ProposalStatus.APPROVED.name(),
                "Can only approve proposal from objection stage"
            );
        }
        this.status = ProposalStatus.APPROVED;
        this.addDecisionEvent(DecisionEventType.PROPOSAL_APPROVED, null, "Proposal approved");
    }
    
    /**
     * 撤回提案
     */
    public void withdraw() {
        if (this.status == ProposalStatus.APPLIED || this.status == ProposalStatus.WITHDRAWN) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                ProposalStatus.WITHDRAWN.name(),
                "Cannot withdraw proposal that is already applied or withdrawn"
            );
        }
        this.status = ProposalStatus.WITHDRAWN;
        this.addDecisionEvent(DecisionEventType.PROPOSAL_WITHDRAWN, this.proposerId, "Proposal withdrawn");
    }
    
    /**
     * 应用提案
     */
    public void apply(PartnerId secretaryId) {
        if (this.status != ProposalStatus.APPROVED) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                ProposalStatus.APPLIED.name(),
                "Only approved proposals can be applied"
            );
        }
        this.status = ProposalStatus.APPLIED;
        this.addDecisionEvent(DecisionEventType.PROPOSAL_APPLIED, secretaryId, "Proposal applied");
    }
    
    /**
     * 添加澄清问题
     */
    public void addClarificationQuestion(Question question) {
        if (this.status != ProposalStatus.CLARIFICATION_STAGE) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                "CLARIFICATION",
                "Can only add questions during clarification stage"
            );
        }
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        this.questions.add(question);
        this.addDecisionEvent(DecisionEventType.QUESTION_ASKED, question.getAskerId(), "Question asked");
    }
    
    /**
     * 添加反应
     */
    public void addReaction(Reaction reaction) {
        if (this.status != ProposalStatus.REACTION_STAGE) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                "REACTION",
                "Can only add reactions during reaction stage"
            );
        }
        if (reaction == null) {
            throw new IllegalArgumentException("Reaction cannot be null");
        }
        this.reactions.add(reaction);
        this.addDecisionEvent(DecisionEventType.REACTION_ADDED, reaction.getReactorId(), "Reaction added");
    }
    
    /**
     * 修改提案
     */
    public void amendProposal(Amendment amendment) {
        if (this.status != ProposalStatus.AMEND_STAGE && this.status != ProposalStatus.INTEGRATION_STAGE) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                "AMEND",
                "Can only amend during amend or integration stage"
            );
        }
        if (amendment == null) {
            throw new IllegalArgumentException("Amendment cannot be null");
        }
        this.amendments.add(amendment);
        this.addDecisionEvent(DecisionEventType.PROPOSAL_AMENDED, this.proposerId, "Proposal amended");
    }
    
    /**
     * 添加反对
     */
    public void addObjection(Objection objection) {
        if (this.status != ProposalStatus.OBJECTION_STAGE) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                "OBJECTION",
                "Can only add objections during objection stage"
            );
        }
        if (objection == null) {
            throw new IllegalArgumentException("Objection cannot be null");
        }
        this.objections.add(objection);
        this.addDecisionEvent(DecisionEventType.OBJECTION_RAISED, objection.getObjectorId(), "Objection raised");
    }
    
    /**
     * 添加投票
     */
    public void addVote(Vote vote) {
        if (vote == null) {
            throw new IllegalArgumentException("Vote cannot be null");
        }
        this.votes.add(vote);
        this.addDecisionEvent(DecisionEventType.VOTE_CAST, vote.getVoterId(), "Vote cast: " + vote.getVoteType());
    }
    
    /**
     * 检查是否有有效反对
     */
    public boolean hasValidObjections() {
        return objections.stream()
            .anyMatch(Objection::isValid);
    }
    
    /**
     * 设置审批流程
     */
    public void setApprovalProcess(ApprovalProcess approvalProcess) {
        if (approvalProcess == null) {
            throw new IllegalArgumentException("Approval process cannot be null");
        }
        if (this.status != ProposalStatus.DRAFT && this.status != ProposalStatus.SUBMITTED) {
            throw new InvalidStateTransitionException(
                this.status.name(),
                "SET_APPROVAL_PROCESS",
                "Can only set approval process for draft or submitted proposals"
            );
        }
        this.approvalProcess = approvalProcess;
    }
    
    /**
     * 检查提案是否满足审批条件
     */
    public boolean meetsApprovalThreshold() {
        return approvalProcess != null && approvalProcess.isApproved(this.votes);
    }
    
    /**
     * 获取决策历史（只读）
     */
    public List<DecisionEvent> getDecisionHistory() {
        return Collections.unmodifiableList(decisionHistory);
    }
    
    /**
     * 获取问题列表（只读）
     */
    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
    
    /**
     * 获取反应列表（只读）
     */
    public List<Reaction> getReactions() {
        return Collections.unmodifiableList(reactions);
    }
    
    /**
     * 获取修改列表（只读）
     */
    public List<Amendment> getAmendments() {
        return Collections.unmodifiableList(amendments);
    }
    
    /**
     * 获取反对列表（只读）
     */
    public List<Objection> getObjections() {
        return Collections.unmodifiableList(objections);
    }
    
    /**
     * 获取投票列表（只读）
     */
    public List<Vote> getVotes() {
        return Collections.unmodifiableList(votes);
    }
    
    /**
     * 添加决策事件
     */
    private void addDecisionEvent(DecisionEventType type, PartnerId actorId, String content) {
        DecisionEvent event = DecisionEvent.create(type, actorId, content);
        this.decisionHistory.add(event);
    }
}
