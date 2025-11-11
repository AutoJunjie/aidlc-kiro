package com.xholacracy.application.dto.proposal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for amendment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmendmentDTO {
    
    private String id;
    
    @NotBlank(message = "Amendment description is required")
    @Size(max = 1000, message = "Amendment must not exceed 1000 characters")
    private String description;
    
    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
    
    private LocalDateTime timestamp;
}
