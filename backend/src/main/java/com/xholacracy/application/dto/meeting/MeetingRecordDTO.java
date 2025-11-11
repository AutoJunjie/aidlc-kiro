package com.xholacracy.application.dto.meeting;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for meeting record
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRecordDTO {
    
    @Size(max = 2000, message = "Check-in notes must not exceed 2000 characters")
    private String checkInNotes;
    
    @Size(max = 2000, message = "Additional notes must not exceed 2000 characters")
    private String additionalNotes;
    
    @Size(max = 2000, message = "Closing notes must not exceed 2000 characters")
    private String closingNotes;
    
    private List<ProposalOutcomeDTO> proposalOutcomes;
}
