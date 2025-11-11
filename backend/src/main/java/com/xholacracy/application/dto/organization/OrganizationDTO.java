package com.xholacracy.application.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for organization
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
    
    private String id;
    private String name;
    private String description;
    private String anchorCircleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
