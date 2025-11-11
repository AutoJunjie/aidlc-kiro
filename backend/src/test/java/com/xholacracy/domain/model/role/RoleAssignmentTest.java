package com.xholacracy.domain.model.role;

import com.xholacracy.domain.exception.ValidationException;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * RoleAssignment实体单元测试
 * 验证角色分配逻辑
 */
class RoleAssignmentTest {
    
    @Test
    void shouldCreateRoleAssignment() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        
        // When
        RoleAssignment assignment = RoleAssignment.create(role, partnerId, assignedBy);
        
        // Then
        assertThat(assignment).isNotNull();
        assertThat(assignment.getId()).isNotNull();
        assertThat(assignment.getRole()).isEqualTo(role);
        assertThat(assignment.getPartnerId()).isEqualTo(partnerId);
        assertThat(assignment.getAssignedBy()).isEqualTo(assignedBy);
        assertThat(assignment.getAssignedDate()).isNotNull();
        assertThat(assignment.getCreatedAt()).isNotNull();
    }
    
    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        // Given
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        
        // When & Then
        assertThatThrownBy(() -> RoleAssignment.create(null, partnerId, assignedBy))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("role");
    }
    
    @Test
    void shouldThrowExceptionWhenPartnerIdIsNull() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId assignedBy = PartnerId.generate();
        
        // When & Then
        assertThatThrownBy(() -> RoleAssignment.create(role, null, assignedBy))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("partnerId");
    }
    
    @Test
    void shouldThrowExceptionWhenAssignedByIsNull() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partnerId = PartnerId.generate();
        
        // When & Then
        assertThatThrownBy(() -> RoleAssignment.create(role, partnerId, null))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("assignedBy");
    }
    
    @Test
    void shouldRecordAssignmentDate() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        
        // When
        RoleAssignment assignment = RoleAssignment.create(role, partnerId, assignedBy);
        
        // Then
        assertThat(assignment.getAssignedDate()).isNotNull();
        assertThat(assignment.getAssignedDate()).isBeforeOrEqualTo(java.time.LocalDateTime.now());
    }
    
    @Test
    void shouldBeEqualWhenIdsAreEqual() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        RoleAssignment assignment1 = RoleAssignment.create(role, partnerId, assignedBy);
        RoleAssignment assignment2 = RoleAssignment.create(role, partnerId, assignedBy);
        
        // When & Then
        assertThat(assignment1).isEqualTo(assignment1);
        assertThat(assignment1).isNotEqualTo(assignment2);
    }
    
    @Test
    void shouldHaveConsistentHashCode() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        RoleAssignment assignment = RoleAssignment.create(role, partnerId, assignedBy);
        
        // When
        int hashCode1 = assignment.hashCode();
        int hashCode2 = assignment.hashCode();
        
        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }
    
    @Test
    void shouldHaveReadableToString() {
        // Given
        Role role = Role.create("Product Manager", "Manage products", CircleId.generate());
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        RoleAssignment assignment = RoleAssignment.create(role, partnerId, assignedBy);
        
        // When
        String result = assignment.toString();
        
        // Then
        assertThat(result).contains("RoleAssignment");
        assertThat(result).contains("partnerId");
        assertThat(result).contains("assignedDate");
    }
}
