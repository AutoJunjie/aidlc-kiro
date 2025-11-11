package com.xholacracy.application.dto.meeting;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Request DTO for creating a governance meeting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingRequest {
    
    @NotNull(message = "Circle ID is required")
    private String circleId;
    
    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;
    
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;
    
    @NotNull(message = "Facilitator ID is required")
    private String facilitatorId;
    
    @NotNull(message = "Secretary ID is required")
    private String secretaryId;
    
    private List<String> participantIds;
}
