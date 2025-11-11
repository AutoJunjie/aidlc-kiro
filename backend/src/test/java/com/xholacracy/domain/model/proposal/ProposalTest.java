package com.xholacracy.domain.model.proposal;

import com.xholacracy.domain.exception.InvalidStateTransitionException;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProposalTest {
    
    @Test
    void shouldCreateProposalWithValidData() {
        // Given
        String title = "Improve communication process";
        Tension tension = Tension.create("Need better communication", "Slow", "Fast");
        ProposalType type = ProposalType.PROCESS_OPTIMIZATION;
        CircleId circleId = CircleId.generate();
        PartnerId proposerId = PartnerId.generate();
        
        // When
        Proposal proposal = Proposal.create(title, tension, type, circleId, proposerId);
        
        // Then
        assertNotNull(proposal);
        assertNotNull(proposal.getId());
        assertEquals(title, proposal.getTitle());
        assertEquals(tension, proposal.getTension());
        assertEquals(type, proposal.getProposalType());
        assertEquals(circleId, proposal.getCircleId());
        assertEquals(proposerId, proposal.getProposerId());
        assertEquals(ProposalStatus.DRAFT, proposal.getStatus());
        assertNotNull(proposal.getCreatedDate());
        assertNull(proposal.getSubmittedDate());
        assertEquals(1, proposal.getDecisionHistory().size());
        assertEquals(DecisionEventType.PROPOSAL_CREATED, proposal.getDecisionHistory().get(0).getEventType());
    }
    
    @Test
    void shouldThrowExceptionWhenTitleIsNull() {
        // Given
        Tension tension = Tension.create("desc", "current", "desired");
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Proposal.create(null, tension, ProposalType.ROLE_MODIFICATION, 
                CircleId.generate(), PartnerId.generate())
        );
    }
    
    @Test
    void shouldThrowExceptionWhenTensionIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Proposal.create("title", null, ProposalType.ROLE_MODIFICATION, 
                CircleId.generate(), PartnerId.generate())
        );
    }
    
    @Test
    void shouldSubmitDraftProposal() {
        // Given
        Proposal proposal = createTestProposal();
        
        // When
        proposal.submit();
        
        // Then
        assertEquals(ProposalStatus.SUBMITTED, proposal.getStatus());
        assertNotNull(proposal.getSubmittedDate());
        assertEquals(2, proposal.getDecisionHistory().size());
        assertEquals(DecisionEventType.PROPOSAL_SUBMITTED, 
            proposal.getDecisionHistory().get(1).getEventType());
    }
    
    @Test
    void shouldThrowExceptionWhenSubmittingNonDraftProposal() {
        // Given
        Proposal proposal = createTestProposal();
        proposal.submit();
        
        // When & Then
        assertThrows(InvalidStateTransitionException.class, proposal::submit);
    }
    
    @Test
    void shouldStartProposalStage() {
        // Given
        Proposal proposal = createTestProposal();
        proposal.submit();
        
        // When
        proposal.startProposalStage();
        
        // Then
        assertEquals(ProposalStatus.PROPOSAL_STAGE, proposal.getStatus());
    }
    
    @Test
    void shouldThrowExceptionWhenStartingProposalStageFromDraft() {
        // Given
        Proposal proposal = createTestProposal();
        
        // When & Then
        assertThrows(InvalidStateTransitionException.class, proposal::startProposalStage);
    }
    
    @Test
    void shouldMoveToClarificationStage() {
        // Given
        Proposal proposal = createTestProposal();
        proposal.submit();
        proposal.startProposalStage();
        
        // When
        proposal.moveToClarificationStage();
        
        // Then
        assertEquals(ProposalStatus.CLARIFICATION_STAGE, proposal.getStatus());
    }
    
    @Test
    void shouldMoveToReactionStage() {
        // Given
        Proposal proposal = createTestProposal();
        proposal.submit();
        proposal.startProposalStage();
        proposal.moveToClarificationStage();
        
        // When
        proposal.moveToReactionStage();
        
        // Then
        assertEquals(ProposalStatus.REACTION_STAGE, proposal.getStatus());
    }
    
    @Test
    void shouldMoveToAmendStage() {
        // Given
        Proposal proposal = createTestProposal();
        proposal.submit();
        proposal.startProposalStage();
        proposal.moveToClarificationStage();
        proposal.moveToReactionStage();
        
        // When
        proposal.moveToAmendStage();
        
        // Then
        assertEquals(ProposalStatus.AMEND_STAGE, proposal.getStatus());
    }
    
    @Test
    void shouldMoveToObjectionStage() {
        // Given
        Proposal proposal = createTestProposal();
        proposal.submit();
        proposal.startProposalStage();
        proposal.moveToClarificationStage();
        proposal.moveToReactionStage();
        proposal.moveToAmendStage();
        
        // When
        proposal.moveToObjectionStage();
        
        // Then
        assertEquals(ProposalStatus.OBJECTION_STAGE, proposal.getStatus());
    }
    
    @Test
    void shouldMoveToIntegrationStage() {
        // Given
        Proposal proposal = createTestProposal();
        moveToObjectionStage(proposal);
        
        // When
        proposal.moveToIntegrationStage();
        
        // Then
        assertEquals(ProposalStatus.INTEGRATION_STAGE, proposal.getStatus());
    }
    
    @Test
    void shouldMoveBackToObjectionStageFromIntegrationStage() {
        // Given
        Proposal proposal = createTestProposal();
        moveToObjectionStage(proposal);
        proposal.moveToIntegrationStage();
        
        // When
        proposal.moveToObjectionStage();
        
        // Then
        assertEquals(ProposalStatus.OBJECTION_STAGE, proposal.getStatus());
    }
    
    @Test
    void shouldApproveProposal() {
        // Given
        Proposal proposal = createTestProposal();
        moveToObjectionStage(proposal);
        
        // When
        proposal.approve();
        
        // Then
        assertEquals(ProposalStatus.APPROVED, proposal.getStatus());
    }
    
    @Test
    void shouldThrowExceptionWhenApprovingFromWrongStage() {
        // Given
        Proposal proposal = createTestProposal();
        
        // When & Then
        assertThrows(InvalidStateTransitionException.class, proposal::approve);
    }
    
    @Test
    void shouldWithdrawProposal() {
        // Given
        Proposal proposal = createTestProposal();
        
        // When
        proposal.withdraw();
        
        // Then
        assertEquals(ProposalStatus.WITHDRAWN, proposal.getStatus());
    }
    
    @Test
    void shouldThrowExceptionWhenWithdrawingAppliedProposal() {
        // Given
        Proposal proposal = createTestProposal();
        moveToObjectionStage(proposal);
        proposal.approve();
        proposal.apply(PartnerId.generate());
        
        // When & Then
        assertThrows(InvalidStateTransitionException.class, proposal::withdraw);
    }
    
    @Test
    void shouldApplyProposal() {
        // Given
        Proposal proposal = createTestProposal();
        moveToObjectionStage(proposal);
        proposal.approve();
        PartnerId secretaryId = PartnerId.generate();
        
        // When
        proposal.apply(secretaryId);
        
        // Then
        assertEquals(ProposalStatus.APPLIED, proposal.getStatus());
    }
    
    @Test
    void shouldThrowExceptionWhenApplyingNonApprovedProposal() {
        // Given
        Proposal proposal = createTestProposal();
        PartnerId secretaryId = PartnerId.generate();
        
        // When & Then
        assertThrows(InvalidStateTransitionException.class, () -> proposal.apply(secretaryId));
    }
    
    @Test
    void shouldRecordDecisionHistory() {
        // Given
        Proposal proposal = createTestProposal();
        
        // When
        proposal.submit();
        proposal.startProposalStage();
        proposal.moveToClarificationStage();
        
        // Then
        assertEquals(4, proposal.getDecisionHistory().size());
        assertEquals(DecisionEventType.PROPOSAL_CREATED, proposal.getDecisionHistory().get(0).getEventType());
        assertEquals(DecisionEventType.PROPOSAL_SUBMITTED, proposal.getDecisionHistory().get(1).getEventType());
        assertEquals(DecisionEventType.STAGE_CHANGED, proposal.getDecisionHistory().get(2).getEventType());
        assertEquals(DecisionEventType.STAGE_CHANGED, proposal.getDecisionHistory().get(3).getEventType());
    }
    
    @Test
    void shouldReturnUnmodifiableDecisionHistory() {
        // Given
        Proposal proposal = createTestProposal();
        
        // When
        List<DecisionEvent> history = proposal.getDecisionHistory();
        
        // Then
        assertThrows(UnsupportedOperationException.class, () -> 
            history.add(DecisionEvent.create(DecisionEventType.VOTE_CAST, null, "test"))
        );
    }
    
    // Helper methods
    
    private Proposal createTestProposal() {
        Tension tension = Tension.create("Need improvement", "Current state", "Desired state");
        return Proposal.create(
            "Test Proposal",
            tension,
            ProposalType.PROCESS_OPTIMIZATION,
            CircleId.generate(),
            PartnerId.generate()
        );
    }
    
    private void moveToObjectionStage(Proposal proposal) {
        proposal.submit();
        proposal.startProposalStage();
        proposal.moveToClarificationStage();
        proposal.moveToReactionStage();
        proposal.moveToAmendStage();
        proposal.moveToObjectionStage();
    }
}
