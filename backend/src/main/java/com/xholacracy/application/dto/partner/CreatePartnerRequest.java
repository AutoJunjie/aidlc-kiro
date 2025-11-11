package com.xholacracy.application.dto.partner;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a partner
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePartnerRequest {
    
    @NotBlank(message = "Partner name is required")
    @Size(min = 2, max = 100, message = "Partner name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
}
