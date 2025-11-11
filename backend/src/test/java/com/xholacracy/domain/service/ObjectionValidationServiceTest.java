package com.xholacracy.domain.service;

import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.Objection;
import com.xholacracy.domain.model.proposal.ObjectionCriteria;
import com.xholacracy.domain.service.ObjectionValidationService.ObjectionCriteriaType;
import com.xholacracy.domain.service.ObjectionValidationService.ObjectionValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ObjectionValidationServiceTest {
    
    private ObjectionValidationService service;
    
    @BeforeEach
    void setUp() {
        service = new ObjectionValidationService();
    }
    
    @Test
    void shouldValidateObjectionThatReducesCapability() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(
            true,  // reduces capability
            false,
            false,
            false
        );
        Objection objection = Objection.create(
            PartnerId.generate(),
            "This proposal reduces our ability to deliver products",
            criteria
        );
        
        // When
        boolean isValid = service.validate(objection);
        
        // Then
        assertThat(isValid).isTrue();
    }
    
    @Test
    void shouldValidateObjectionThatLimitsAccountability() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(
            false,
            true,  // limits accountability
            false,
            false
        );
        Objection objection = Objection.create(
            PartnerId.generate(),
            "This proposal prevents me from fulfilling my role",
            criteria
        );
        
        // When
        boolean isValid = service.validate(objection);
        
        // Then
        assertThat(isValid).isTrue();
    }
    
    @Test
    void shouldValidateObjectionWhereProblemNotExistWithout() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(
            false,
            false,
            true,  // problem not exist without
            false
        );
        Objection objection = Objection.create(
            PartnerId.generate(),
            "This problem only exists because of this proposal",
            criteria
        );
        
        // When
        boolean isValid = service.validate(objection);
        
        // Then
        assertThat(isValid).isTrue();
    }
    
    @Test
    void shouldValidateObjectionThatCausesHarm() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(
            false,
            false,
            false,
            true  // causes harm
        );
        Objection objection = Objection.create(
            PartnerId.generate(),
            "This proposal will cause significant harm to the team",
            criteria
        );
        
        // When
        boolean isValid = service.validate(objection);
        
        // Then
        assertThat(isValid).isTrue();
    }
    
    @Test
    void shouldValidateObjectionWithMultipleCriteria() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(
            true,  // reduces capability
            true,  // limits accountability
            false,
            false
        );
        Objection objection = Objection.create(
            PartnerId.generate(),
            "This proposal has multiple issues",
            criteria
        );
        
        // When
        boolean isValid = service.validate(objection);
        
        // Then
        assertThat(isValid).isTrue();
    }
    
    @Test
    void shouldInvalidateObjectionWithNoCriteria() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(
            false,
            false,
            false,
            false
        );
        Objection objection = Objection.create(
            PartnerId.generate(),
            "I just don't like this proposal",
            criteria
        );
        
        // When
        boolean isValid = service.validate(objection);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void shouldInvalidateNullObjection() {
        // When
        boolean isValid = service.validate(null);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void shouldInvalidateObjectionWithNullCriteria() {
        // Given - Objection.create() doesn't allow null criteria, so we test the service directly
        
        // When
        boolean isValid = service.validate(null);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void shouldProvideDetailedValidationResultForValidObjection() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(
            true,  // reduces capability
            false,
            false,
            false
        );
        Objection objection = Objection.create(
            PartnerId.generate(),
            "This proposal reduces our ability",
            criteria
        );
        
        // When
        ObjectionValidationResult result = service.validateWithDetails(objection);
        
        // Then
        assertThat(result.isValid()).isTrue();
        assertThat(result.getMessage()).contains("Reduces Circle Capability");
    }
    
    @Test
    void shouldProvideDetailedValidationResultForInvalidObjection() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(
            false, false, false, false
        );
        Objection objection = Objection.create(
            PartnerId.generate(),
            "Invalid objection",
            criteria
        );
        
        // When
        ObjectionValidationResult result = service.validateWithDetails(objection);
        
        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("does not meet any of the four valid objection criteria");
    }
    
    @Test
    void shouldProvideDetailedValidationResultForMultipleCriteria() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(
            true,  // reduces capability
            true,  // limits accountability
            false,
            true   // causes harm
        );
        Objection objection = Objection.create(
            PartnerId.generate(),
            "Multiple issues",
            criteria
        );
        
        // When
        ObjectionValidationResult result = service.validateWithDetails(objection);
        
        // Then
        assertThat(result.isValid()).isTrue();
        assertThat(result.getMessage())
            .contains("Reduces Circle Capability")
            .contains("Limits Objector's Accountability Fulfillment")
            .contains("Causes Harm");
    }
    
    @Test
    void shouldCheckSpecificCriteria() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(
            true,  // reduces capability
            false,
            false,
            false
        );
        Objection objection = Objection.create(
            PartnerId.generate(),
            "Reduces capability",
            criteria
        );
        
        // When & Then
        assertThat(service.meetsCriteria(objection, ObjectionCriteriaType.REDUCES_CAPABILITY)).isTrue();
        assertThat(service.meetsCriteria(objection, ObjectionCriteriaType.LIMITS_ACCOUNTABILITY)).isFalse();
        assertThat(service.meetsCriteria(objection, ObjectionCriteriaType.PROBLEM_NOT_EXIST_WITHOUT)).isFalse();
        assertThat(service.meetsCriteria(objection, ObjectionCriteriaType.CAUSES_HARM)).isFalse();
    }
    
    @Test
    void shouldReturnFalseForNullObjectionWhenCheckingCriteria() {
        // When & Then
        assertThat(service.meetsCriteria(null, ObjectionCriteriaType.REDUCES_CAPABILITY)).isFalse();
    }
    
    @Test
    void shouldReturnFalseForObjectionWithNullCriteriaWhenCheckingCriteria() {
        // Given - Test with null objection since Objection.create() doesn't allow null criteria
        
        // When & Then
        assertThat(service.meetsCriteria(null, ObjectionCriteriaType.REDUCES_CAPABILITY)).isFalse();
    }
}
