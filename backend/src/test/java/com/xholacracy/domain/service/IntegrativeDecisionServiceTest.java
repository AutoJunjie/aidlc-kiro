package com.xholacracy.domain.service;

import com.xholacracy.domain.exception.InvalidStateTransitionException;
import com.xholacracy.domain.exception.ResourceNotFoundException;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntegrativeDecisionServiceTest {
    
    @Mock
    private ProposalRepository proposalRepository;
    
    @Mock
    private ObjectionValidationService objectionValidationService;
    
    private IntegrativeDecisionService service;
    
    @BeforeEach
    void setUp() {
        service = new IntegrativeDecisionService(proposalRepository, objectionValidationService);
    }
    
    @Test
    void shouldApproveProposalWhenNoValidObjections() {
        // Given
        Proposal proposal = createProposalInObjectionStage();
        Objection objection = createObjection(proposal.getProposerId(), "Invalid objection");
        proposal.addObjection(objection);
        
        when(proposalRepository.findById(proposal.getId())).thenReturn(Optional.of(proposal));
        when(objectionValidationService.validate(any(Objection.class))).thenReturn(false);
        
        // When
        service.processObjectionStage(proposal.getId());
        
        // Then
        assertThat(proposal.getStatus()).isEqualTo(ProposalStatus.APPROVED);
        verify(proposalRepository).save(proposal);
    }
    
    @Test
    void shouldMoveToIntegrationStageWhenValidObjectionsExist() {
        // Given
        Proposal proposal = createProposalInObjectionStage();
        Objection validObjection = createObjection(proposal.getProposerId(), "Valid objection");
        proposal.addObjection(validObjection);
        
        when(proposalRepository.findById(proposal.getId())).thenReturn(Optional.of(proposal));
        when(objectionValidationService.validate(any(Objection.class))).thenReturn(true);
        
        // When
        service.processObjectionStage(proposal.getId());
        
        // Then
        assertThat(proposal.getStatus()).isEqualTo(ProposalStatus.INTEGRATION_STAGE);
        verify(proposalRepository).save(proposal);
    }
    
    @Test
    void shouldThrowExceptionWhenProposalNotFound() {
        // Given
        ProposalId proposalId = ProposalId.generate();
        when(proposalRepository.findById(proposalId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> service.processObjectionStage(proposalId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Proposal");
    }
    
    @Test
    void shouldThrowExceptionWhenProposalNotInObjectionStage() {
        // Given
        Proposal proposal = createDraftProposal();
        when(proposalRepository.findById(proposal.getId())).thenReturn(Optional.of(proposal));
        
        // When & Then
        assertThatThrownBy(() -> service.processObjectionStage(proposal.getId()))
            .isInstanceOf(InvalidStateTransitionException.class);
    }
    
    @Test
    void shouldReturnToObjectionStageAfterIntegration() {
        // Given
        Proposal proposal = createProposalInIntegrationStage();
        when(proposalRepository.findById(proposal.getId())).thenReturn(Optional.of(proposal));
        
        // When
        service.processIntegrationStage(proposal.getId());
        
        // Then
        assertThat(proposal.getStatus()).isEqualTo(ProposalStatus.OBJECTION_STAGE);
        verify(proposalRepository).save(proposal);
    }
    
    @Test
    void shouldThrowExceptionWhenProposalNotInIntegrationStage() {
        // Given
        Proposal proposal = createDraftProposal();
        when(proposalRepository.findById(proposal.getId())).thenReturn(Optional.of(proposal));
        
        // When & Then
        assertThatThrownBy(() -> service.processIntegrationStage(proposal.getId()))
            .isInstanceOf(InvalidStateTransitionException.class);
    }
    
    @Test
    void shouldValidateTransitionFromDraftToSubmitted() {
        // Given
        Proposal proposal = createDraftProposal();
        
        // When
        boolean canTransition = service.canTransitionTo(proposal, ProposalStatus.SUBMITTED);
        
        // Then
        assertThat(canTransition).isTrue();
    }
    
    @Test
    void shouldNotAllowInvalidTransition() {
        // Given
        Proposal proposal = createDraftProposal();
        
        // When
        boolean canTransition = service.canTransitionTo(proposal, ProposalStatus.APPROVED);
        
        // Then
        assertThat(canTransition).isFalse();
    }
    
    @Test
    void shouldAllowTransitionFromObjectionToApproved() {
        // Given
        Proposal proposal = createProposalInObjectionStage();
        
        // When
        boolean canTransition = service.canTransitionTo(proposal, ProposalStatus.APPROVED);
        
        // Then
        assertThat(canTransition).isTrue();
    }
    
    @Test
    void shouldAllowTransitionFromObjectionToIntegration() {
        // Given
        Proposal proposal = createProposalInObjectionStage();
        
        // When
        boolean canTransition = service.canTransitionTo(proposal, ProposalStatus.INTEGRATION_STAGE);
        
        // Then
        assertThat(canTransition).isTrue();
    }
    
    @Test
    void shouldNotAllowTransitionFromAppliedState() {
        // Given
        Proposal proposal = createAppliedProposal();
        
        // When
        boolean canTransition = service.canTransitionTo(proposal, ProposalStatus.DRAFT);
        
        // Then
        assertThat(canTransition).isFalse();
    }
    
    @Test
    void shouldAllowProceedToNextStageForSubmittedProposal() {
        // Given
        Proposal proposal = createSubmittedProposal();
        
        // When
        boolean canProceed = service.canProceedToNextStage(proposal);
        
        // Then
        assertThat(canProceed).isTrue();
    }
    
    @Test
    void shouldNotAllowProceedToNextStageForDraftProposal() {
        // Given
        Proposal proposal = createDraftProposal();
        
        // When
        boolean canProceed = service.canProceedToNextStage(proposal);
        
        // Then
        assertThat(canProceed).isFalse();
    }
    
    @Test
    void shouldNotAllowProceedToNextStageForAppliedProposal() {
        // Given
        Proposal proposal = createAppliedProposal();
        
        // When
        boolean canProceed = service.canProceedToNextStage(proposal);
        
        // Then
        assertThat(canProceed).isFalse();
    }
    
    // Helper methods
    
    private Proposal createDraftProposal() {
        Tension tension = Tension.create(
            "Need product role",
            "No dedicated product role",
            "Have clear product role"
        );
        
        return Proposal.create(
            "Create Product Role",
            tension,
            ProposalType.ROLE_MODIFICATION,
            CircleId.generate(),
            PartnerId.generate()
        );
    }
    
    private Proposal createSubmittedProposal() {
        Proposal proposal = createDraftProposal();
        proposal.submit();
        return proposal;
    }
    
    private Proposal createProposalInObjectionStage() {
        Proposal proposal = createDraftProposal();
        proposal.submit();
        proposal.startProposalStage();
        proposal.moveToClarificationStage();
        proposal.moveToReactionStage();
        proposal.moveToAmendStage();
        proposal.moveToObjectionStage();
        return proposal;
    }
    
    private Proposal createProposalInIntegrationStage() {
        Proposal proposal = createProposalInObjectionStage();
        proposal.moveToIntegrationStage();
        return proposal;
    }
    
    private Proposal createAppliedProposal() {
        Proposal proposal = createProposalInObjectionStage();
        proposal.approve();
        proposal.apply(PartnerId.generate());
        return proposal;
    }
    
    private Objection createObjection(PartnerId objectorId, String reasoning) {
        ObjectionCriteria criteria = ObjectionCriteria.create(
            false, false, false, false
        );
        return Objection.create(objectorId, reasoning, criteria);
    }
}
