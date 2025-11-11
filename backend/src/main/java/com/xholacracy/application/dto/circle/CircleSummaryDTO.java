package com.xholacracy.application.dto.circle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Summary DTO for circle (used in lists and hierarchies)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CircleSummaryDTO {
    
    private String id;
    private String name;
    private String purpose;
    private int roleCount;
    private int subCircleCount;
}
