package com.xholacracy.application.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for meeting agenda
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingAgendaDTO {
    
    private List<AgendaItemDTO> items;
}
