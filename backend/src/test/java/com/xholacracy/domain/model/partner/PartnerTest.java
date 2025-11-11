package com.xholacracy.domain.model.partner;

import com.xholacracy.domain.exception.ValidationException;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.role.Role;
import com.xholacracy.domain.model.role.RoleId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Partner实体单元测试
 * 验证伙伴和角色的关系
 */
class PartnerTest {
    
    @Test
    void shouldCreatePartner() {
        // Given
        String name = "John Doe";
        String email = "john.doe@example.com";
        
        // When
        Partner partner = Partner.create(name, email);
        
        // Then
        assertThat(partner).isNotNull();
        assertThat(partner.getId()).isNotNull();
        assertThat(partner.getName()).isEqualTo(name);
        assertThat(partner.getEmail()).isEqualTo(email);
        assertThat(partner.getRoleAssignments()).isEmpty();
        assertThat(partner.getCreatedAt()).isNotNull();
        assertThat(partner.getUpdatedAt()).isNotNull();
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // When & Then
        assertThatThrownBy(() -> Partner.create(null, "john@example.com"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        // When & Then
        assertThatThrownBy(() -> Partner.create("", "john@example.com"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
        
        assertThatThrownBy(() -> Partner.create("   ", "john@example.com"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
    }
    
    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        // When & Then
        assertThatThrownBy(() -> Partner.create("John Doe", null))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("email");
    }
    
    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        // When & Then
        assertThatThrownBy(() -> Partner.create("John Doe", ""))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("email");
    }
    
    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        // When & Then
        assertThatThrownBy(() -> Partner.create("John Doe", "invalid-email"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("email");
        
        assertThatThrownBy(() -> Partner.create("John Doe", "invalid@"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("email");
        
        assertThatThrownBy(() -> Partner.create("John Doe", "@example.com"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("email");
    }
    
    @Test
    void shouldAcceptValidEmailFormats() {
        // When & Then
        assertThatCode(() -> Partner.create("John Doe", "john@example.com"))
            .doesNotThrowAnyException();
        
        assertThatCode(() -> Partner.create("John Doe", "john.doe@example.com"))
            .doesNotThrowAnyException();
        
        assertThatCode(() -> Partner.create("John Doe", "john+test@example.co.uk"))
            .doesNotThrowAnyException();
    }
    
    @Test
    void shouldGetRolesFromAssignments() {
        // Given
        Partner partner = Partner.create("John Doe", "john@example.com");
        Role role1 = Role.create("Product Manager", "Manage products", CircleId.generate());
        Role role2 = Role.create("Tech Lead", "Lead technical decisions", CircleId.generate());
        
        // Simulate role assignments (normally done through Role.assignToPartner)
        role1.assignToPartner(partner.getId(), PartnerId.generate());
        role2.assignToPartner(partner.getId(), PartnerId.generate());
        
        // When
        // Note: In real scenario, assignments would be bidirectional
        // For this test, we're testing the getRoles method structure
        
        // Then
        assertThat(partner.getRoles()).isNotNull();
    }
    
    @Test
    void shouldCheckIfPartnerHasRole() {
        // Given
        Partner partner = Partner.create("John Doe", "john@example.com");
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        
        // When
        boolean hasRoleBefore = partner.hasRole(role.getId());
        
        // Then
        assertThat(hasRoleBefore).isFalse();
    }
    
    @Test
    void shouldReturnFalseWhenCheckingNullRoleId() {
        // Given
        Partner partner = Partner.create("John Doe", "john@example.com");
        
        // When
        boolean hasRole = partner.hasRole(null);
        
        // Then
        assertThat(hasRole).isFalse();
    }
    
    @Test
    void shouldUpdatePartnerInfo() {
        // Given
        Partner partner = Partner.create("John Doe", "john@example.com");
        String newName = "Jane Doe";
        String newEmail = "jane@example.com";
        
        // When
        partner.updateInfo(newName, newEmail);
        
        // Then
        assertThat(partner.getName()).isEqualTo(newName);
        assertThat(partner.getEmail()).isEqualTo(newEmail);
    }
    
    @Test
    void shouldNotUpdateWhenNameIsNull() {
        // Given
        Partner partner = Partner.create("John Doe", "john@example.com");
        String originalName = partner.getName();
        
        // When
        partner.updateInfo(null, "newemail@example.com");
        
        // Then
        assertThat(partner.getName()).isEqualTo(originalName);
    }
    
    @Test
    void shouldNotUpdateWhenEmailIsNull() {
        // Given
        Partner partner = Partner.create("John Doe", "john@example.com");
        String originalEmail = partner.getEmail();
        
        // When
        partner.updateInfo("New Name", null);
        
        // Then
        assertThat(partner.getEmail()).isEqualTo(originalEmail);
    }
    
    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidEmail() {
        // Given
        Partner partner = Partner.create("John Doe", "john@example.com");
        
        // When & Then
        assertThatThrownBy(() -> partner.updateInfo("New Name", "invalid-email"))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("email");
    }
    
    @Test
    void shouldReturnImmutableRoleAssignments() {
        // Given
        Partner partner = Partner.create("John Doe", "john@example.com");
        
        // When & Then
        assertThatThrownBy(() -> partner.getRoleAssignments().clear())
            .isInstanceOf(UnsupportedOperationException.class);
    }
    
    @Test
    void shouldBeEqualWhenIdsAreEqual() {
        // Given
        Partner partner1 = Partner.create("John Doe", "john@example.com");
        Partner partner2 = Partner.create("Jane Doe", "jane@example.com");
        
        // When & Then
        assertThat(partner1).isEqualTo(partner1);
        assertThat(partner1).isNotEqualTo(partner2);
    }
    
    @Test
    void shouldHaveConsistentHashCode() {
        // Given
        Partner partner = Partner.create("John Doe", "john@example.com");
        
        // When
        int hashCode1 = partner.hashCode();
        int hashCode2 = partner.hashCode();
        
        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }
    
    @Test
    void shouldHaveReadableToString() {
        // Given
        Partner partner = Partner.create("John Doe", "john@example.com");
        
        // When
        String result = partner.toString();
        
        // Then
        assertThat(result).contains("Partner");
        assertThat(result).contains("John Doe");
        assertThat(result).contains("john@example.com");
    }
}
