package com.xholacracy.application.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for assigning a role to a partner
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleRequest {
    
    @NotNull(message = "Partner ID is required")
    private String partnerId;
    
    @NotNull(message = "Assigned by partner ID is required")
    private String assignedBy;
}
