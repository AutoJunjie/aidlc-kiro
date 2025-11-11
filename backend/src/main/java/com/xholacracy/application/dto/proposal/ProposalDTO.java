package com.xholacracy.application.dto.proposal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for proposal
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDTO {
    
    private String id;
    private String title;
    private TensionDTO tension;
    private String proposalType;
    private String circleId;
    private String proposerId;
    private String proposerName;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime submittedDate;
    private List<DecisionEventDTO> decisionHistory;
    private List<QuestionDTO> questions;
    private List<ReactionDTO> reactions;
    private List<AmendmentDTO> amendments;
    private List<ObjectionDTO> objections;
    private List<VoteDTO> votes;
}
