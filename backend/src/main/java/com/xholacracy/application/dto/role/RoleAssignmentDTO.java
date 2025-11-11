package com.xholacracy.application.dto.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for role assignment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleAssignmentDTO {
    
    private String id;
    private String roleId;
    private String partnerId;
    private String partnerName;
    private String assignedBy;
    private LocalDateTime assignedDate;
}
