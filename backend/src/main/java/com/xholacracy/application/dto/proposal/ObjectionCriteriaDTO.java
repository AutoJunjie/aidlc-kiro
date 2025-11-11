package com.xholacracy.application.dto.proposal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for objection criteria
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectionCriteriaDTO {
    
    private boolean reducesCapability;
    private boolean limitsAccountability;
    private boolean problemNotExistWithout;
    private boolean causesHarm;
}
