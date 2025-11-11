package com.xholacracy.application.dto.meeting;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for proposal outcome
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalOutcomeDTO {
    
    @NotNull(message = "Proposal ID is required")
    private String proposalId;
    
    private String proposalTitle;
    
    @NotNull(message = "Outcome is required")
    private String outcome;
    
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
}
