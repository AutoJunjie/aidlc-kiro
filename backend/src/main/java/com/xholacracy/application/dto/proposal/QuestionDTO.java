package com.xholacracy.application.dto.proposal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for clarification question
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    
    private String id;
    
    @NotBlank(message = "Question is required")
    @Size(max = 500, message = "Question must not exceed 500 characters")
    private String question;
    
    @Size(max = 1000, message = "Answer must not exceed 1000 characters")
    private String answer;
    
    private String askerId;
    private String askerName;
    private LocalDateTime timestamp;
}
