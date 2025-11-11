package com.xholacracy.application.dto.proposal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for vote
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    
    private String id;
    
    @NotNull(message = "Vote type is required")
    private String voteType;
    
    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String comment;
    
    private String voterId;
    private String voterName;
    private LocalDateTime timestamp;
}
