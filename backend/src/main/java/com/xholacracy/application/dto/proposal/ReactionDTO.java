package com.xholacracy.application.dto.proposal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for reaction
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDTO {
    
    private String id;
    
    @NotBlank(message = "Reaction content is required")
    @Size(max = 1000, message = "Reaction must not exceed 1000 characters")
    private String content;
    
    private String reactorId;
    private String reactorName;
    private int orderIndex;
    private LocalDateTime timestamp;
}
