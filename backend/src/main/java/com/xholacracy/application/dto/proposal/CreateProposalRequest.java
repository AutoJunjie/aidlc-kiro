package com.xholacracy.application.dto.proposal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a proposal
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProposalRequest {
    
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;
    
    @NotNull(message = "Tension is required")
    @Valid
    private TensionDTO tension;
    
    @NotNull(message = "Proposal type is required")
    private String proposalType;
    
    @NotNull(message = "Circle ID is required")
    private String circleId;
    
    @NotNull(message = "Proposer ID is required")
    private String proposerId;
}
