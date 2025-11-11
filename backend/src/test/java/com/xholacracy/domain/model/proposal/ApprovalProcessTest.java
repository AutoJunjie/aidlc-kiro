package com.xholacracy.domain.model.proposal;

import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApprovalProcessTest {
    
    // ApprovalThreshold Tests
    
    @Test
    void shouldCreateApprovalThreshold() {
        // When
        ApprovalThreshold threshold = ApprovalThreshold.create(3, 1, 60, false);
        
        // Then
        assertNotNull(threshold);
        assertEquals(3, threshold.getMinApproveVotes());
        assertEquals(1, threshold.getMaxObjectVotes());
        assertEquals(60, threshold.getMinApprovePercentage());
        assertFalse(threshold.getRequiresUnanimous());
    }
    
    @Test
    void shouldThrowExceptionWhenMinApproveVotesIsNegative() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            ApprovalThreshold.create(-1, 1, 60, false)
        );
    }
    
    @Test
    void shouldThrowExceptionWhenPercentageIsInvalid() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            ApprovalThreshold.create(3, 1, 101, false)
        );
        assertThrows(IllegalArgumentException.class, () ->
            ApprovalThreshold.create(3, 1, -1, false)
        );
    }
    
    @Test
    void shouldCreateSimpleMajorityThreshold() {
        // When
        ApprovalThreshold threshold = ApprovalThreshold.simpleMajority();
        
        // Then
        assertNotNull(threshold);
        assertEquals(51, threshold.getMinApprovePercentage());
        assertFalse(threshold.getRequiresUnanimous());
    }
    
    @Test
    void shouldCreateUnanimousThreshold() {
        // When
        ApprovalThreshold threshold = ApprovalThreshold.unanimous();
        
        // Then
        assertNotNull(threshold);
        assertEquals(0, threshold.getMaxObjectVotes());
        assertEquals(100, threshold.getMinApprovePercentage());
        assertTrue(threshold.getRequiresUnanimous());
    }
    
    @Test
    void shouldMeetThresholdWithSimpleMajority() {
        // Given
        ApprovalThreshold threshold = ApprovalThreshold.simpleMajority();
        
        // When & Then
        assertTrue(threshold.isMet(6, 4, 10)); // 60% approve
        assertTrue(threshold.isMet(51, 49, 100)); // 51% approve
        assertFalse(threshold.isMet(5, 5, 10)); // 50% approve
        assertFalse(threshold.isMet(4, 6, 10)); // 40% approve
    }
    
    @Test
    void shouldMeetThresholdWithUnanimous() {
        // Given
        ApprovalThreshold threshold = ApprovalThreshold.unanimous();
        
        // When & Then
        assertTrue(threshold.isMet(10, 0, 10)); // All approve
        assertFalse(threshold.isMet(9, 1, 10)); // One objection
        assertFalse(threshold.isMet(9, 0, 10)); // One abstain
    }
    
    @Test
    void shouldMeetThresholdWithMinApproveVotes() {
        // Given
        ApprovalThreshold threshold = ApprovalThreshold.create(5, null, null, false);
        
        // When & Then
        assertTrue(threshold.isMet(5, 0, 10));
        assertTrue(threshold.isMet(6, 0, 10));
        assertFalse(threshold.isMet(4, 0, 10));
    }
    
    @Test
    void shouldMeetThresholdWithMaxObjectVotes() {
        // Given
        ApprovalThreshold threshold = ApprovalThreshold.create(null, 2, null, false);
        
        // When & Then
        assertTrue(threshold.isMet(8, 2, 10));
        assertTrue(threshold.isMet(9, 1, 10));
        assertFalse(threshold.isMet(7, 3, 10));
    }
    
    @Test
    void shouldNotMeetThresholdWithZeroVotes() {
        // Given
        ApprovalThreshold threshold = ApprovalThreshold.simpleMajority();
        
        // When & Then
        assertFalse(threshold.isMet(0, 0, 0));
    }
    
    // ApprovalProcess Tests
    
    @Test
    void shouldCreateApprovalProcess() {
        // Given
        ApprovalThreshold threshold = ApprovalThreshold.simpleMajority();
        List<PartnerId> approvers = List.of(PartnerId.generate(), PartnerId.generate());
        Duration timeLimit = Duration.ofHours(48);
        
        // When
        ApprovalProcess process = ApprovalProcess.create(threshold, approvers, timeLimit);
        
        // Then
        assertNotNull(process);
        assertEquals(threshold, process.getApprovalThreshold());
        assertEquals(2, process.getRequiredApproverIds().size());
        assertEquals(48L, process.getTimeLimitHours());
    }
    
    @Test
    void shouldThrowExceptionWhenThresholdIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            ApprovalProcess.create(null, new ArrayList<>(), null)
        );
    }
    
    @Test
    void shouldCreateDefaultApprovalProcess() {
        // When
        ApprovalProcess process = ApprovalProcess.defaultProcess();
        
        // Then
        assertNotNull(process);
        assertNotNull(process.getApprovalThreshold());
        assertTrue(process.getRequiredApproverIds().isEmpty());
        assertNull(process.getTimeLimitHours());
    }
    
    @Test
    void shouldCheckIfProposalIsApproved() {
        // Given
        ApprovalProcess process = ApprovalProcess.defaultProcess();
        List<Vote> votes = List.of(
            Vote.create(PartnerId.generate(), VoteType.APPROVE),
            Vote.create(PartnerId.generate(), VoteType.APPROVE),
            Vote.create(PartnerId.generate(), VoteType.APPROVE),
            Vote.create(PartnerId.generate(), VoteType.APPROVE),
            Vote.create(PartnerId.generate(), VoteType.APPROVE),
            Vote.create(PartnerId.generate(), VoteType.APPROVE),
            Vote.create(PartnerId.generate(), VoteType.OBJECT),
            Vote.create(PartnerId.generate(), VoteType.OBJECT),
            Vote.create(PartnerId.generate(), VoteType.OBJECT),
            Vote.create(PartnerId.generate(), VoteType.OBJECT)
        );
        
        // When
        boolean isApproved = process.isApproved(votes);
        
        // Then
        assertTrue(isApproved); // 60% approve
    }
    
    @Test
    void shouldNotBeApprovedWithInsufficientVotes() {
        // Given
        ApprovalProcess process = ApprovalProcess.defaultProcess();
        List<Vote> votes = List.of(
            Vote.create(PartnerId.generate(), VoteType.APPROVE),
            Vote.create(PartnerId.generate(), VoteType.OBJECT)
        );
        
        // When
        boolean isApproved = process.isApproved(votes);
        
        // Then
        assertFalse(isApproved); // 50% approve
    }
    
    @Test
    void shouldNotBeApprovedWithNoVotes() {
        // Given
        ApprovalProcess process = ApprovalProcess.defaultProcess();
        
        // When
        boolean isApproved = process.isApproved(new ArrayList<>());
        
        // Then
        assertFalse(isApproved);
    }
    
    @Test
    void shouldCheckIfAllRequiredApproversVoted() {
        // Given
        PartnerId approver1 = PartnerId.generate();
        PartnerId approver2 = PartnerId.generate();
        ApprovalProcess process = ApprovalProcess.create(
            ApprovalThreshold.simpleMajority(),
            List.of(approver1, approver2),
            null
        );
        
        List<Vote> votes = List.of(
            Vote.create(approver1, VoteType.APPROVE),
            Vote.create(approver2, VoteType.APPROVE),
            Vote.create(PartnerId.generate(), VoteType.APPROVE)
        );
        
        // When
        boolean allVoted = process.allRequiredApproversVoted(votes);
        
        // Then
        assertTrue(allVoted);
    }
    
    @Test
    void shouldReturnTrueWhenNoRequiredApprovers() {
        // Given
        ApprovalProcess process = ApprovalProcess.defaultProcess();
        
        // When
        boolean allVoted = process.allRequiredApproversVoted(new ArrayList<>());
        
        // Then
        assertTrue(allVoted);
    }
    
    @Test
    void shouldSetApprovalProcessOnProposal() {
        // Given
        Proposal proposal = createTestProposal();
        ApprovalProcess customProcess = ApprovalProcess.create(
            ApprovalThreshold.unanimous(),
            new ArrayList<>(),
            Duration.ofHours(24)
        );
        
        // When
        proposal.setApprovalProcess(customProcess);
        
        // Then
        assertEquals(customProcess, proposal.getApprovalProcess());
    }
    
    @Test
    void shouldCheckIfProposalMeetsApprovalThreshold() {
        // Given
        Proposal proposal = createTestProposal();
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.APPROVE));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.APPROVE));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.APPROVE));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.APPROVE));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.APPROVE));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.APPROVE));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.OBJECT));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.OBJECT));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.OBJECT));
        proposal.addVote(Vote.create(PartnerId.generate(), VoteType.OBJECT));
        
        // When
        boolean meetsThreshold = proposal.meetsApprovalThreshold();
        
        // Then
        assertTrue(meetsThreshold); // 60% approve with simple majority
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
}
