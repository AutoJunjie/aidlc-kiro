package com.xholacracy.application.dto.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for role
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    
    private String id;
    private String name;
    private String purpose;
    private List<String> accountabilities;
    private List<DomainDTO> domains;
    private String circleId;
    private boolean isSpecialRole;
    private String specialRoleType;
    private List<RoleAssignmentDTO> assignments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
