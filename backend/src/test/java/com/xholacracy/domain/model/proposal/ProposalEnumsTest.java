package com.xholacracy.domain.model.proposal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProposalEnumsTest {
    
    @Test
    void shouldHaveAllProposalTypes() {
        // When
        ProposalType[] types = ProposalType.values();
        
        // Then
        assertEquals(4, types.length);
        assertNotNull(ProposalType.valueOf("ROLE_MODIFICATION"));
        assertNotNull(ProposalType.valueOf("POLICY_ADJUSTMENT"));
        assertNotNull(ProposalType.valueOf("CIRCLE_STRUCTURE_CHANGE"));
        assertNotNull(ProposalType.valueOf("PROCESS_OPTIMIZATION"));
    }
    
    @Test
    void shouldHaveAllProposalStatuses() {
        // When
        ProposalStatus[] statuses = ProposalStatus.values();
        
        // Then
        assertEquals(12, statuses.length);
        assertNotNull(ProposalStatus.valueOf("DRAFT"));
        assertNotNull(ProposalStatus.valueOf("SUBMITTED"));
        assertNotNull(ProposalStatus.valueOf("PROPOSAL_STAGE"));
        assertNotNull(ProposalStatus.valueOf("CLARIFICATION_STAGE"));
        assertNotNull(ProposalStatus.valueOf("REACTION_STAGE"));
        assertNotNull(ProposalStatus.valueOf("AMEND_STAGE"));
        assertNotNull(ProposalStatus.valueOf("OBJECTION_STAGE"));
        assertNotNull(ProposalStatus.valueOf("INTEGRATION_STAGE"));
        assertNotNull(ProposalStatus.valueOf("APPROVED"));
        assertNotNull(ProposalStatus.valueOf("APPLIED"));
        assertNotNull(ProposalStatus.valueOf("WITHDRAWN"));
        assertNotNull(ProposalStatus.valueOf("REJECTED"));
    }
    
    @Test
    void shouldHaveAllDecisionEventTypes() {
        // When
        DecisionEventType[] eventTypes = DecisionEventType.values();
        
        // Then
        assertEquals(15, eventTypes.length);
        assertNotNull(DecisionEventType.valueOf("PROPOSAL_CREATED"));
        assertNotNull(DecisionEventType.valueOf("PROPOSAL_SUBMITTED"));
        assertNotNull(DecisionEventType.valueOf("STAGE_CHANGED"));
        assertNotNull(DecisionEventType.valueOf("QUESTION_ASKED"));
        assertNotNull(DecisionEventType.valueOf("QUESTION_ANSWERED"));
        assertNotNull(DecisionEventType.valueOf("REACTION_ADDED"));
        assertNotNull(DecisionEventType.valueOf("PROPOSAL_AMENDED"));
        assertNotNull(DecisionEventType.valueOf("OBJECTION_RAISED"));
        assertNotNull(DecisionEventType.valueOf("OBJECTION_VALIDATED"));
        assertNotNull(DecisionEventType.valueOf("OBJECTION_INVALIDATED"));
        assertNotNull(DecisionEventType.valueOf("VOTE_CAST"));
        assertNotNull(DecisionEventType.valueOf("PROPOSAL_APPROVED"));
        assertNotNull(DecisionEventType.valueOf("PROPOSAL_APPLIED"));
        assertNotNull(DecisionEventType.valueOf("PROPOSAL_WITHDRAWN"));
        assertNotNull(DecisionEventType.valueOf("PROPOSAL_REJECTED"));
    }
    
    @Test
    void shouldHaveAllVoteTypes() {
        // When
        VoteType[] voteTypes = VoteType.values();
        
        // Then
        assertEquals(3, voteTypes.length);
        assertNotNull(VoteType.valueOf("APPROVE"));
        assertNotNull(VoteType.valueOf("OBJECT"));
        assertNotNull(VoteType.valueOf("ABSTAIN"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidProposalType() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            ProposalType.valueOf("INVALID_TYPE")
        );
    }
    
    @Test
    void shouldThrowExceptionForInvalidProposalStatus() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            ProposalStatus.valueOf("INVALID_STATUS")
        );
    }
    
    @Test
    void shouldThrowExceptionForInvalidVoteType() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            VoteType.valueOf("INVALID_VOTE")
        );
    }
}
