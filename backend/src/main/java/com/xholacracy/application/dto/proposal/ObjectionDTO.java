package com.xholacracy.application.dto.proposal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for objection
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectionDTO {
    
    private String id;
    
    @NotBlank(message = "Reasoning is required")
    @Size(max = 1000, message = "Reasoning must not exceed 1000 characters")
    private String reasoning;
    
    private String objectorId;
    private String objectorName;
    private boolean isValid;
    private String validatedBy;
    private ObjectionCriteriaDTO criteria;
    private LocalDateTime timestamp;
}
