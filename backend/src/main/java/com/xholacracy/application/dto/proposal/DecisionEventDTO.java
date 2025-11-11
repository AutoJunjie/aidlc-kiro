package com.xholacracy.application.dto.proposal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for decision event
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecisionEventDTO {
    
    private String eventType;
    private LocalDateTime timestamp;
    private String actorId;
    private String actorName;
    private String content;
}
