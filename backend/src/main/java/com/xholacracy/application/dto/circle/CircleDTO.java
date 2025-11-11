package com.xholacracy.application.dto.circle;

import com.xholacracy.application.dto.role.RoleSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for circle
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CircleDTO {
    
    private String id;
    private String name;
    private String purpose;
    private List<String> accountabilities;
    private String parentCircleId;
    private String organizationId;
    private List<RoleSummaryDTO> roles;
    private List<CircleSummaryDTO> subCircles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
