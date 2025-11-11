package com.xholacracy.application.dto.circle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for creating a circle
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCircleRequest {
    
    @NotBlank(message = "Circle name is required")
    @Size(min = 2, max = 100, message = "Circle name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Purpose is required")
    @Size(max = 500, message = "Purpose must not exceed 500 characters")
    private String purpose;
    
    private List<@Size(max = 200) String> accountabilities;
    
    private String parentCircleId;
    
    @NotNull(message = "Organization ID is required")
    private String organizationId;
}
