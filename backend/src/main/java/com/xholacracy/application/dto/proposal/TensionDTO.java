package com.xholacracy.application.dto.proposal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for tension
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TensionDTO {
    
    @NotBlank(message = "Tension description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotBlank(message = "Current state is required")
    @Size(max = 500, message = "Current state must not exceed 500 characters")
    private String currentState;
    
    @NotBlank(message = "Desired state is required")
    @Size(max = 500, message = "Desired state must not exceed 500 characters")
    private String desiredState;
    
    private List<@Size(max = 200) String> examples;
    
    @Size(max = 1000, message = "Context must not exceed 1000 characters")
    private String context;
}
