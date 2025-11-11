package com.xholacracy.application.dto.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Summary DTO for role (used in lists)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleSummaryDTO {
    
    private String id;
    private String name;
    private String purpose;
    private boolean isSpecialRole;
    private String specialRoleType;
    private int assignmentCount;
}
