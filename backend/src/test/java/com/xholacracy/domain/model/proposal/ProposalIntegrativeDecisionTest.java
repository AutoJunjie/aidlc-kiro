package com.xholacracy.domain.model.proposal;

import com.xholacracy.domain.exception.InvalidStateTransitionException;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProposalIntegrativeDecisionTest {
    
    @Test
    void shouldAddClarificationQuestion() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.CLARIFICATION_STAGE);
        Question question = Question.create(PartnerId.generate(), "Can you clarify?");
        
        // When
        proposal.addClarificationQuestion(question);
        
        // Then
        assertEquals(1, proposal.getQuestions().size());
        assertEquals(question, proposal.getQuestions().get(0));
    }
    
    @Test
    void shouldThrowExceptionWhenAddingQuestionInWrongStage() {
        // Given
        Proposal proposal = createTestProposal();
        Question question = Question.create(PartnerId.generate(), "Question?");
        
        // When & Then
        assertThrows(InvalidStateTransitionException.class, () ->
            proposal.addClarificationQuestion(question)
        );
    }
    
    @Test
    void shouldAddMultipleQuestions() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.CLARIFICATION_STAGE);
        
        // When
        proposal.addClarificationQuestion(Question.create(PartnerId.generate(), "Question 1?"));
        proposal.addClarificationQuestion(Question.create(PartnerId.generate(), "Question 2?"));
        proposal.addClarificationQuestion(Question.create(PartnerId.generate(), "Question 3?"));
        
        // Then
        assertEquals(3, proposal.getQuestions().size());
    }
    
    @Test
    void shouldAddReaction() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.REACTION_STAGE);
        Reaction reaction = Reaction.create(PartnerId.generate(), "Good idea", 0);
        
        // When
        proposal.addReaction(reaction);
        
        // Then
        assertEquals(1, proposal.getReactions().size());
        assertEquals(reaction, proposal.getReactions().get(0));
    }
    
    @Test
    void shouldThrowExceptionWhenAddingReactionInWrongStage() {
        // Given
        Proposal proposal = createTestProposal();
        Reaction reaction = Reaction.create(PartnerId.generate(), "Reaction", 0);
        
        // When & Then
        assertThrows(InvalidStateTransitionException.class, () ->
            proposal.addReaction(reaction)
        );
    }
    
    @Test
    void shouldAddMultipleReactionsInOrder() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.REACTION_STAGE);
        
        // When
        proposal.addReaction(Reaction.create(PartnerId.generate(), "Reaction 1", 0));
        proposal.addReaction(Reaction.create(PartnerId.generate(), "Reaction 2", 1));
        proposal.addReaction(Reaction.create(PartnerId.generate(), "Reaction 3", 2));
        
        // Then
        assertEquals(3, proposal.getReactions().size());
        assertEquals(0, proposal.getReactions().get(0).getOrderIndex());
        assertEquals(1, proposal.getReactions().get(1).getOrderIndex());
        assertEquals(2, proposal.getReactions().get(2).getOrderIndex());
    }
    
    @Test
    void shouldAmendProposalInAmendStage() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.AMEND_STAGE);
        Amendment amendment = Amendment.create("Changed role name", "Based on feedback");
        
        // When
        proposal.amendProposal(amendment);
        
        // Then
        assertEquals(1, proposal.getAmendments().size());
        assertEquals(amendment, proposal.getAmendments().get(0));
    }
    
    @Test
    void shouldAmendProposalInIntegrationStage() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.INTEGRATION_STAGE);
        Amendment amendment = Amendment.create("Resolved objection", "Addressed concerns");
        
        // When
        proposal.amendProposal(amendment);
        
        // Then
        assertEquals(1, proposal.getAmendments().size());
    }
    
    @Test
    void shouldThrowExceptionWhenAmendingInWrongStage() {
        // Given
        Proposal proposal = createTestProposal();
        Amendment amendment = Amendment.create("Change", "Reason");
        
        // When & Then
        assertThrows(InvalidStateTransitionException.class, () ->
            proposal.amendProposal(amendment)
        );
    }
    
    @Test
    void shouldAddObjection() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.OBJECTION_STAGE);
        ObjectionCriteria criteria = ObjectionCriteria.create(true, false, false, false);
        Objection objection = Objection.create(PartnerId.generate(), "This will cause problems", criteria);
        
        // When
        proposal.addObjection(objection);
        
        // Then
        assertEquals(1, proposal.getObjections().size());
        assertEquals(objection, proposal.getObjections().get(0));
    }
    
    @Test
    void shouldThrowExceptionWhenAddingObjectionInWrongStage() {
        // Given
        Proposal proposal = createTestProposal();
        ObjectionCriteria criteria = ObjectionCriteria.create(true, false, false, false);
        Objection objection = Objection.create(PartnerId.generate(), "Objection", criteria);
        
        // When & Then
        assertThrows(InvalidStateTransitionException.class, () ->
            proposal.addObjection(objection)
        );
    }
    
    @Test
    void shouldAddVote() {
        // Given
        Proposal proposal = createTestProposal();
        Vote vote = Vote.create(PartnerId.generate(), VoteType.APPROVE, "I support this");
        
        // When
        proposal.addVote(vote);
        
        // Then
        assertEquals(1, proposal.getVotes().size());
        assertEquals(vote, proposal.getVotes().get(0));
    }
    
    @Test
    void shouldAddMultipleVotes() {
        // Given
        Proposal proposal = createTestProposal();
        
        // When
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.APPROVE));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.APPROVE));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.OBJECT));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.ABSTAIN));
        
        // Then
        assertEquals(4, proposal.getVotes().size());
    }
    
    @Test
    void shouldDetectValidObjections() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.OBJECTION_STAGE);
        
        ObjectionCriteria validCriteria = ObjectionCriteria.create(true, false, false, false);
        Objection objection = Objection.create(PartnerId.generate(), "Valid objection", validCriteria);
        objection.validate(PartnerId.generate(), true);
        
        proposal.addObjection(objection);
        
        // When
        boolean hasValid = proposal.hasValidObjections();
        
        // Then
        assertTrue(hasValid);
    }
    
    @Test
    void shouldNotDetectValidObjectionsWhenNoneExist() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.OBJECTION_STAGE);
        
        ObjectionCriteria criteria = ObjectionCriteria.create(true, false, false, false);
        Objection objection = Objection.create(PartnerId.generate(), "Invalid objection", criteria);
        objection.validate(PartnerId.generate(), false);
        
        proposal.addObjection(objection);
        
        // When
        boolean hasValid = proposal.hasValidObjections();
        
        // Then
        assertFalse(hasValid);
    }
    
    @Test
    void shouldNotDetectValidObjectionsWhenListIsEmpty() {
        // Given
        Proposal proposal = createTestProposal();
        
        // When
        boolean hasValid = proposal.hasValidObjections();
        
        // Then
        assertFalse(hasValid);
    }
    
    @Test
    void shouldReturnUnmodifiableQuestionsList() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.CLARIFICATION_STAGE);
        proposal.addClarificationQuestion(Question.create(PartnerId.generate(), "Question?"));
        
        // When & Then
        assertThrows(UnsupportedOperationException.class, () ->
            proposal.getQuestions().add(Question.create(PartnerId.generate(), "Another?"))
        );
    }
    
    @Test
    void shouldReturnUnmodifiableReactionsList() {
        // Given
        Proposal proposal = createTestProposal();
        moveToStage(proposal, ProposalStatus.REACTION_STAGE);
        proposal.addReaction(Reaction.create(PartnerId.generate(), "Reaction", 0));
        
        // When & Then
        assertThrows(UnsupportedOperationException.class, () ->
            proposal.getReactions().add(Reaction.create(PartnerId.generate(), "Another", 1))
        );
    }
    
    @Test
    void shouldCompleteFullIntegrativeDecisionProcess() {
        // Given
        Proposal proposal = createTestProposal();
        
        // Submit and start
        proposal.submit();
        proposal.startProposalStage();
        
        // Clarification stage
        proposal.moveToClarificationStage();
        proposal.addClarificationQuestion(Question.create(PartnerId.generate(), "Question 1?"));
        proposal.addClarificationQuestion(Question.create(PartnerId.generate(), "Question 2?"));
        
        // Reaction stage
        proposal.moveToReactionStage();
        proposal.addReaction(Reaction.create(PartnerId.generate(), "Reaction 1", 0));
        proposal.addReaction(Reaction.create(PartnerId.generate(), "Reaction 2", 1));
        
        // Amend stage
        proposal.moveToAmendStage();
        proposal.amendProposal(Amendment.create("Amendment 1", "Reason 1"));
        
        // Objection stage
        proposal.moveToObjectionStage();
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.APPROVE));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.APPROVE));
        
        // Approve
        proposal.approve();
        
        // Then
        assertEquals(ProposalStatus.APPROVED, proposal.getStatus());
        assertEquals(2, proposal.getQuestions().size());
        assertEquals(2, proposal.getReactions().size());
        assertEquals(1, proposal.getAmendments().size());
        assertEquals(2, proposal.getVotes().size());
    }
    
    // Helper methods
    
    private Proposal createTestProposal() {
        Tension tension = Tension.create("Need improvement", "Current", "Desired");
        return Proposal.create(
            "Test Proposal",
            tension,
            ProposalType.PROCESS_OPTIMIZATION,
            CircleId.generate(),
            PartnerId.generate()
        );
    }
    
    private void moveToStage(Proposal proposal, ProposalStatus targetStage) {
        if (proposal.getStatus() == ProposalStatus.DRAFT) {
            proposal.submit();
        }
        if (proposal.getStatus() == ProposalStatus.SUBMITTED && targetStage != ProposalStatus.SUBMITTED) {
            proposal.startProposalStage();
        }
        if (proposal.getStatus() == ProposalStatus.PROPOSAL_STAGE && targetStage != ProposalStatus.PROPOSAL_STAGE) {
            proposal.moveToClarificationStage();
        }
        if (proposal.getStatus() == ProposalStatus.CLARIFICATION_STAGE && targetStage != ProposalStatus.CLARIFICATION_STAGE) {
            proposal.moveToReactionStage();
        }
        if (proposal.getStatus() == ProposalStatus.REACTION_STAGE && targetStage != ProposalStatus.REACTION_STAGE) {
            proposal.moveToAmendStage();
        }
        if (proposal.getStatus() == ProposalStatus.AMEND_STAGE && targetStage != ProposalStatus.AMEND_STAGE) {
            proposal.moveToObjectionStage();
        }
        if (proposal.getStatus() == ProposalStatus.OBJECTION_STAGE && targetStage == ProposalStatus.INTEGRATION_STAGE) {
            proposal.moveToIntegrationStage();
        }
    }
}
