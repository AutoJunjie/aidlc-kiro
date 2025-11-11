package com.xholacracy.domain.service;

import com.xholacracy.domain.exception.InvalidStateTransitionException;
import com.xholacracy.domain.exception.ResourceNotFoundException;
import com.xholacracy.domain.model.proposal.Objection;
import com.xholacracy.domain.model.proposal.Proposal;
import com.xholacracy.domain.model.proposal.ProposalId;
import com.xholacracy.domain.model.proposal.ProposalRepository;
import com.xholacracy.domain.model.proposal.ProposalStatus;

/**
 * 集成决策流程领域服务
 * 负责协调提案在集成决策流程中的状态转换和业务规则验证
 */
public class IntegrativeDecisionService {
    
    private final ProposalRepository proposalRepository;
    private final ObjectionValidationService objectionValidationService;
    
    public IntegrativeDecisionService(
            ProposalRepository proposalRepository,
            ObjectionValidationService objectionValidationService) {
        this.proposalRepository = proposalRepository;
        this.objectionValidationService = objectionValidationService;
    }
    
    /**
     * 处理反对阶段
     * 验证所有反对意见的有效性，如果没有有效反对则批准提案，否则进入集成阶段
     * 
     * @param proposalId 提案ID
     * @throws ResourceNotFoundException 如果提案不存在
     * @throws InvalidStateTransitionException 如果提案不在反对阶段
     */
    public void processObjectionStage(ProposalId proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
            .orElseThrow(() -> new ResourceNotFoundException("Proposal", proposalId.getValue()));
        
        if (proposal.getStatus() != ProposalStatus.OBJECTION_STAGE) {
            throw new InvalidStateTransitionException(
                proposal.getStatus().name(),
                "OBJECTION_STAGE_PROCESSING"
            );
        }
        
        // 验证所有反对意见
        boolean hasValidObjections = false;
        for (Objection objection : proposal.getObjections()) {
            boolean isValid = objectionValidationService.validate(objection);
            // Note: In a real implementation, the facilitator would validate objections
            // For now, we just check if they meet the criteria
            if (isValid) {
                hasValidObjections = true;
            }
        }
        
        // 如果没有有效反对，批准提案
        if (!hasValidObjections) {
            proposal.approve();
        } else {
            // 有有效反对，进入集成阶段
            proposal.moveToIntegrationStage();
        }
        
        proposalRepository.save(proposal);
    }
    
    /**
     * 处理集成阶段
     * 在集成阶段，参与者协作解决反对意见
     * 提案人可以修改提案以解决反对，修改后返回反对阶段重新验证
     * 
     * @param proposalId 提案ID
     * @throws ResourceNotFoundException 如果提案不存在
     * @throws InvalidStateTransitionException 如果提案不在集成阶段
     */
    public void processIntegrationStage(ProposalId proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
            .orElseThrow(() -> new ResourceNotFoundException("Proposal", proposalId.getValue()));
        
        if (proposal.getStatus() != ProposalStatus.INTEGRATION_STAGE) {
            throw new InvalidStateTransitionException(
                proposal.getStatus().name(),
                "INTEGRATION_STAGE_PROCESSING"
            );
        }
        
        // 集成阶段完成后，返回反对阶段重新验证
        // 这个方法由外部调用者在集成讨论完成并修改提案后调用
        proposal.moveToObjectionStage();
        
        proposalRepository.save(proposal);
    }
    
    /**
     * 验证提案状态转换是否合法
     * 
     * @param proposal 提案
     * @param targetStatus 目标状态
     * @return 是否可以转换
     */
    public boolean canTransitionTo(Proposal proposal, ProposalStatus targetStatus) {
        ProposalStatus currentStatus = proposal.getStatus();
        
        return switch (currentStatus) {
            case DRAFT -> targetStatus == ProposalStatus.SUBMITTED;
            case SUBMITTED -> targetStatus == ProposalStatus.PROPOSAL_STAGE;
            case PROPOSAL_STAGE -> targetStatus == ProposalStatus.CLARIFICATION_STAGE;
            case CLARIFICATION_STAGE -> targetStatus == ProposalStatus.REACTION_STAGE;
            case REACTION_STAGE -> targetStatus == ProposalStatus.AMEND_STAGE;
            case AMEND_STAGE -> targetStatus == ProposalStatus.OBJECTION_STAGE;
            case OBJECTION_STAGE -> 
                targetStatus == ProposalStatus.APPROVED || 
                targetStatus == ProposalStatus.INTEGRATION_STAGE;
            case INTEGRATION_STAGE -> 
                targetStatus == ProposalStatus.OBJECTION_STAGE || 
                targetStatus == ProposalStatus.WITHDRAWN;
            case APPROVED -> targetStatus == ProposalStatus.APPLIED;
            case APPLIED, WITHDRAWN, REJECTED -> false;
        };
    }
    
    /**
     * 检查提案是否可以进入下一阶段
     * 
     * @param proposal 提案
     * @return 是否可以进入下一阶段
     */
    public boolean canProceedToNextStage(Proposal proposal) {
        return switch (proposal.getStatus()) {
            case DRAFT -> false; // 需要先提交
            case SUBMITTED -> true; // 可以开始提案阶段
            case PROPOSAL_STAGE -> true; // 提案陈述完成后可以进入澄清阶段
            case CLARIFICATION_STAGE -> true; // 澄清完成后可以进入反应阶段
            case REACTION_STAGE -> true; // 反应完成后可以进入修改阶段
            case AMEND_STAGE -> true; // 修改完成后可以进入反对阶段
            case OBJECTION_STAGE -> true; // 反对阶段完成后根据结果决定
            case INTEGRATION_STAGE -> true; // 集成完成后返回反对阶段
            case APPROVED -> false; // 已批准，等待应用
            case APPLIED, WITHDRAWN, REJECTED -> false; // 终态
        };
    }
}
