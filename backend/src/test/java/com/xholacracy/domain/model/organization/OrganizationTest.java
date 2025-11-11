package com.xholacracy.domain.model.organization;

import com.xholacracy.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Organization聚合根单元测试
 * 验证业务规则和行为
 */
class OrganizationTest {
    
    @Test
    void shouldCreateOrganizationWithAnchorCircle() {
        // Given
        String name = "Tech Company";
        String description = "A technology company";
        
        // When
        Organization organization = Organization.create(name, description);
        
        // Then
        assertThat(organization).isNotNull();
        assertThat(organization.getId()).isNotNull();
        assertThat(organization.getName()).isEqualTo(name);
        assertThat(organization.getDescription()).isEqualTo(description);
        
        // 验证自动创建了Anchor Circle
        assertThat(organization.getAnchorCircle()).isNotNull();
        assertThat(organization.getAnchorCircle().getName()).isEqualTo("Anchor Circle");
        assertThat(organization.getAnchorCircleId()).isNotNull();
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // When & Then
        assertThatThrownBy(() -> Organization.create(null, "Description"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name")
            .hasMessageContaining("cannot be null or empty");
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        // When & Then
        assertThatThrownBy(() -> Organization.create("", "Description"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
        
        assertThatThrownBy(() -> Organization.create("   ", "Description"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsTooLong() {
        // Given
        String longName = "a".repeat(256);
        
        // When & Then
        assertThatThrownBy(() -> Organization.create(longName, "Description"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name")
            .hasMessageContaining("cannot exceed 255 characters");
    }
    
    @Test
    void shouldUpdateOrganizationInfo() {
        // Given
        Organization organization = Organization.create("Old Name", "Old Description");
        String newName = "New Name";
        String newDescription = "New Description";
        
        // When
        organization.updateInfo(newName, newDescription);
        
        // Then
        assertThat(organization.getName()).isEqualTo(newName);
        assertThat(organization.getDescription()).isEqualTo(newDescription);
    }
    
    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidName() {
        // Given
        Organization organization = Organization.create("Valid Name", "Description");
        
        // When & Then
        assertThatThrownBy(() -> organization.updateInfo(null, "New Description"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
        
        assertThatThrownBy(() -> organization.updateInfo("", "New Description"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
    }
    
    @Test
    void shouldBeEqualWhenIdsAreEqual() {
        // Given
        Organization org1 = Organization.create("Company 1", "Description 1");
        Organization org2 = Organization.create("Company 2", "Description 2");
        
        // When & Then
        assertThat(org1).isEqualTo(org1);
        assertThat(org1).isNotEqualTo(org2);
    }
    
    @Test
    void shouldHaveConsistentHashCode() {
        // Given
        Organization organization = Organization.create("Company", "Description");
        
        // When
        int hashCode1 = organization.hashCode();
        int hashCode2 = organization.hashCode();
        
        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }
    
    @Test
    void shouldHaveReadableToString() {
        // Given
        Organization organization = Organization.create("Tech Company", "Description");
        
        // When
        String result = organization.toString();
        
        // Then
        assertThat(result).contains("Organization");
        assertThat(result).contains("Tech Company");
        assertThat(result).contains(organization.getId().toString());
    }
    
    @Test
    void shouldAllowNullDescription() {
        // When
        Organization organization = Organization.create("Company", null);
        
        // Then
        assertThat(organization.getDescription()).isNull();
    }
    
    @Test
    void shouldAllowEmptyDescription() {
        // When
        Organization organization = Organization.create("Company", "");
        
        // Then
        assertThat(organization.getDescription()).isEmpty();
    }
}
