package com.xholacracy.domain.model.role;

import com.xholacracy.domain.exception.ValidationException;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.circle.SpecialRoleType;
import com.xholacracy.domain.model.partner.PartnerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Role实体单元测试
 * 验证角色创建和分配逻辑
 */
class RoleTest {
    
    @Test
    void shouldCreateRegularRole() {
        // Given
        String name = "Product Manager";
        String purpose = "Manage product development";
        CircleId circleId = CircleId.generate();
        
        // When
        Role role = Role.create(name, purpose, circleId);
        
        // Then
        assertThat(role).isNotNull();
        assertThat(role.getId()).isNotNull();
        assertThat(role.getName()).isEqualTo(name);
        assertThat(role.getPurpose()).isEqualTo(purpose);
        assertThat(role.getCircleId()).isEqualTo(circleId);
        assertThat(role.isSpecialRole()).isFalse();
        assertThat(role.getSpecialRoleType()).isNull();
        assertThat(role.getAccountabilities()).isEmpty();
        assertThat(role.getDomains()).isEmpty();
        assertThat(role.getAssignments()).isEmpty();
        assertThat(role.getCreatedAt()).isNotNull();
        assertThat(role.getUpdatedAt()).isNotNull();
    }
    
    @Test
    void shouldCreateSpecialRole() {
        // Given
        String name = "Circle Lead";
        CircleId circleId = CircleId.generate();
        SpecialRoleType type = SpecialRoleType.CIRCLE_LEAD;
        
        // When
        Role role = Role.createSpecialRole(name, circleId, type);
        
        // Then
        assertThat(role).isNotNull();
        assertThat(role.getId()).isNotNull();
        assertThat(role.getName()).isEqualTo(name);
        assertThat(role.getPurpose()).isEqualTo(type.getDescription());
        assertThat(role.getCircleId()).isEqualTo(circleId);
        assertThat(role.isSpecialRole()).isTrue();
        assertThat(role.getSpecialRoleType()).isEqualTo(type);
    }
    
    @Test
    void shouldThrowExceptionWhenRoleNameIsNull() {
        // Given
        CircleId circleId = CircleId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Role.create(null, "Purpose", circleId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
    }
    
    @Test
    void shouldThrowExceptionWhenRoleNameIsEmpty() {
        // Given
        CircleId circleId = CircleId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Role.create("", "Purpose", circleId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
        
        assertThatThrownBy(() -> Role.create("   ", "Purpose", circleId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
    }
    
    @Test
    void shouldThrowExceptionWhenRolePurposeIsNull() {
        // Given
        CircleId circleId = CircleId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Role.create("Name", null, circleId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("purpose");
    }
    
    @Test
    void shouldThrowExceptionWhenRolePurposeIsEmpty() {
        // Given
        CircleId circleId = CircleId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Role.create("Name", "", circleId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("purpose");
    }
    
    @Test
    void shouldThrowExceptionWhenCircleIdIsNull() {
        // When & Then
        assertThatThrownBy(() -> Role.create("Name", "Purpose", null))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("circleId");
    }
    
    @Test
    void shouldThrowExceptionWhenSpecialRoleTypeIsNull() {
        // Given
        CircleId circleId = CircleId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Role.createSpecialRole("Name", circleId, null))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("SpecialRoleType");
    }
    
    @Test
    void shouldAssignRoleToPartner() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        
        // When
        RoleAssignment assignment = role.assignToPartner(partnerId, assignedBy);
        
        // Then
        assertThat(assignment).isNotNull();
        assertThat(assignment.getPartnerId()).isEqualTo(partnerId);
        assertThat(assignment.getAssignedBy()).isEqualTo(assignedBy);
        assertThat(assignment.getAssignedDate()).isNotNull();
        assertThat(role.getAssignments()).hasSize(1);
        assertThat(role.getAssignments()).contains(assignment);
    }
    
    @Test
    void shouldThrowExceptionWhenAssigningToNullPartner() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId assignedBy = PartnerId.generate();
        
        // When & Then
        assertThatThrownBy(() -> role.assignToPartner(null, assignedBy))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("partnerId");
    }
    
    @Test
    void shouldThrowExceptionWhenAssignedByIsNull() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partnerId = PartnerId.generate();
        
        // When & Then
        assertThatThrownBy(() -> role.assignToPartner(partnerId, null))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("assignedBy");
    }
    
    @Test
    void shouldAddDomain() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        Domain domain = Domain.create("Product Roadmap", "Control over product roadmap");
        
        // When
        role.addDomain(domain);
        
        // Then
        assertThat(role.getDomains()).hasSize(1);
        assertThat(role.getDomains()).contains(domain);
    }
    
    @Test
    void shouldThrowExceptionWhenAddingNullDomain() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        
        // When & Then
        assertThatThrownBy(() -> role.addDomain(null))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("domain");
    }
    
    @Test
    void shouldAddAccountability() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        String accountability = "Define product vision";
        
        // When
        role.addAccountability(accountability);
        
        // Then
        assertThat(role.getAccountabilities()).hasSize(1);
        assertThat(role.getAccountabilities()).contains(accountability);
    }
    
    @Test
    void shouldThrowExceptionWhenAddingNullAccountability() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        
        // When & Then
        assertThatThrownBy(() -> role.addAccountability(null))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("accountability");
    }
    
    @Test
    void shouldThrowExceptionWhenAddingEmptyAccountability() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        
        // When & Then
        assertThatThrownBy(() -> role.addAccountability(""))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("accountability");
    }
    
    @Test
    void shouldCheckIfRoleIsAssignedToPartner() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        role.assignToPartner(partnerId, assignedBy);
        
        // When
        boolean isAssigned = role.isAssignedTo(partnerId);
        boolean isNotAssigned = role.isAssignedTo(PartnerId.generate());
        
        // Then
        assertThat(isAssigned).isTrue();
        assertThat(isNotAssigned).isFalse();
    }
    
    @Test
    void shouldReturnFalseWhenCheckingNullPartnerId() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        
        // When
        boolean isAssigned = role.isAssignedTo(null);
        
        // Then
        assertThat(isAssigned).isFalse();
    }
    
    @Test
    void shouldUpdateRoleInfo() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        String newName = "Senior Product Manager";
        String newPurpose = "Lead product strategy";
        
        // When
        role.updateInfo(newName, newPurpose);
        
        // Then
        assertThat(role.getName()).isEqualTo(newName);
        assertThat(role.getPurpose()).isEqualTo(newPurpose);
    }
    
    @Test
    void shouldRemoveAssignment() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        RoleAssignment assignment = role.assignToPartner(partnerId, assignedBy);
        
        // When
        role.removeAssignment(assignment);
        
        // Then
        assertThat(role.getAssignments()).isEmpty();
    }
    
    @Test
    void shouldReturnImmutableCollections() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        
        // When & Then
        assertThatThrownBy(() -> role.getAccountabilities().clear())
            .isInstanceOf(UnsupportedOperationException.class);
        
        assertThatThrownBy(() -> role.getDomains().clear())
            .isInstanceOf(UnsupportedOperationException.class);
        
        assertThatThrownBy(() -> role.getAssignments().clear())
            .isInstanceOf(UnsupportedOperationException.class);
    }
    
    @Test
    void shouldBeEqualWhenIdsAreEqual() {
        // Given
        Role role1 = Role.create("Product Manager", "Manage products", CircleId.generate());
        Role role2 = Role.create("Product Manager", "Manage products", CircleId.generate());
        
        // When & Then
        assertThat(role1).isEqualTo(role1);
        assertThat(role1).isNotEqualTo(role2);
    }
    
    @Test
    void shouldHaveConsistentHashCode() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        
        // When
        int hashCode1 = role.hashCode();
        int hashCode2 = role.hashCode();
        
        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }
    
    @Test
    void shouldHaveReadableToString() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        
        // When
        String result = role.toString();
        
        // Then
        assertThat(result).contains("Role");
        assertThat(result).contains("Product Manager");
        assertThat(result).contains("isSpecialRole=false");
    }
    
    @Test
    void shouldAssignMultiplePartnersToSameRole() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partner1 = PartnerId.generate();
        PartnerId partner2 = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        
        // When
        role.assignToPartner(partner1, assignedBy);
        role.assignToPartner(partner2, assignedBy);
        
        // Then
        assertThat(role.getAssignments()).hasSize(2);
        assertThat(role.isAssignedTo(partner1)).isTrue();
        assertThat(role.isAssignedTo(partner2)).isTrue();
    }
}
