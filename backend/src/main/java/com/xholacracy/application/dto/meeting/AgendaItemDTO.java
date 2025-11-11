package com.xholacracy.application.dto.meeting;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for agenda item
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendaItemDTO {
    
    private String id;
    
    @NotNull(message = "Proposal ID is required")
    private String proposalId;
    
    private String proposalTitle;
    private int orderIndex;
    private String status;
}
