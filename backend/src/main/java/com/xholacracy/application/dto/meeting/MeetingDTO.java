package com.xholacracy.application.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for governance meeting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDTO {
    
    private String id;
    private String circleId;
    private String circleName;
    private LocalDateTime scheduledDate;
    private Integer durationMinutes;
    private String status;
    private String facilitatorId;
    private String facilitatorName;
    private String secretaryId;
    private String secretaryName;
    private List<String> participantIds;
    private List<String> participantNames;
    private MeetingAgendaDTO agenda;
    private MeetingRecordDTO record;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
