package com.xholacracy.domain.model.circle;

import com.xholacracy.domain.model.organization.OrganizationId;
import com.xholacracy.domain.exception.DomainException;
import com.xholacracy.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Circle聚合根单元测试
 * 验证圈子层次结构和特殊角色创建
 */
class CircleTest {
    
    @Test
    void shouldCreateAnchorCircleWithSpecialRoles() {
        // Given
        OrganizationId orgId = OrganizationId.generate();
        
        // When
        Circle anchorCircle = Circle.createAnchorCircle(orgId);
        
        // Then
        assertThat(anchorCircle).isNotNull();
        assertThat(anchorCircle.getId()).isNotNull();
        assertThat(anchorCircle.getName()).isEqualTo("Anchor Circle");
        assertThat(anchorCircle.getPurpose()).isNotEmpty();
        assertThat(anchorCircle.getOrganizationId()).isEqualTo(orgId);
        assertThat(anchorCircle.getParentCircleId()).isNull();
        assertThat(anchorCircle.isAnchorCircle()).isTrue();
        
        // 验证自动创建了四个特殊角色
        assertThat(anchorCircle.getRoles()).hasSize(4);
        assertThat(anchorCircle.getSpecialRoles()).isNotNull();
        assertThat(anchorCircle.getSpecialRoles().getCircleLeadRoleId()).isNotNull();
        assertThat(anchorCircle.getSpecialRoles().getFacilitatorRoleId()).isNotNull();
        assertThat(anchorCircle.getSpecialRoles().getSecretaryRoleId()).isNotNull();
        assertThat(anchorCircle.getSpecialRoles().getCircleRepRoleId()).isNotNull();
        
        // 验证所有角色都是特殊角色
        anchorCircle.getRoles().forEach(role -> {
            assertThat(role.isSpecialRole()).isTrue();
            assertThat(role.getSpecialRoleType()).isNotNull();
        });
    }
    
    @Test
    void shouldCreateSubCircleWithSpecialRoles() {
        // Given
        String name = "Product Circle";
        String purpose = "Manage product development";
        CircleId parentId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        // When
        Circle subCircle = Circle.createSubCircle(name, purpose, parentId, orgId);
        
        // Then
        assertThat(subCircle).isNotNull();
        assertThat(subCircle.getId()).isNotNull();
        assertThat(subCircle.getName()).isEqualTo(name);
        assertThat(subCircle.getPurpose()).isEqualTo(purpose);
        assertThat(subCircle.getParentCircleId()).isEqualTo(parentId);
        assertThat(subCircle.getOrganizationId()).isEqualTo(orgId);
        assertThat(subCircle.isAnchorCircle()).isFalse();
        
        // 验证自动创建了四个特殊角色
        assertThat(subCircle.getRoles()).hasSize(4);
        assertThat(subCircle.getSpecialRoles()).isNotNull();
    }
    
    @Test
    void shouldThrowExceptionWhenSubCircleNameIsNull() {
        // Given
        CircleId parentId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Circle.createSubCircle(null, "Purpose", parentId, orgId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
    }
    
    @Test
    void shouldThrowExceptionWhenSubCircleNameIsEmpty() {
        // Given
        CircleId parentId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Circle.createSubCircle("", "Purpose", parentId, orgId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
        
        assertThatThrownBy(() -> Circle.createSubCircle("   ", "Purpose", parentId, orgId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name");
    }
    
    @Test
    void shouldThrowExceptionWhenSubCirclePurposeIsNull() {
        // Given
        CircleId parentId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Circle.createSubCircle("Name", null, parentId, orgId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("purpose");
    }
    
    @Test
    void shouldThrowExceptionWhenSubCirclePurposeIsEmpty() {
        // Given
        CircleId parentId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Circle.createSubCircle("Name", "", parentId, orgId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("purpose");
    }
    
    @Test
    void shouldThrowExceptionWhenParentCircleIdIsNull() {
        // Given
        OrganizationId orgId = OrganizationId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Circle.createSubCircle("Name", "Purpose", null, orgId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Parent circle ID");
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsTooLong() {
        // Given
        String longName = "a".repeat(256);
        CircleId parentId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Circle.createSubCircle(longName, "Purpose", parentId, orgId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("name")
            .hasMessageContaining("cannot exceed 255 characters");
    }
    
    @Test
    void shouldThrowExceptionWhenPurposeIsTooLong() {
        // Given
        String longPurpose = "a".repeat(1001);
        CircleId parentId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Circle.createSubCircle("Name", longPurpose, parentId, orgId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("purpose")
            .hasMessageContaining("cannot exceed 1000 characters");
    }
    
    @Test
    void shouldUpdateCircleInfo() {
        // Given
        Circle circle = Circle.createAnchorCircle(OrganizationId.generate());
        String newName = "New Name";
        String newPurpose = "New Purpose";
        List<String> newAccountabilities = Arrays.asList("Accountability 1", "Accountability 2");
        
        // When
        circle.updateInfo(newName, newPurpose, newAccountabilities);
        
        // Then
        assertThat(circle.getName()).isEqualTo(newName);
        assertThat(circle.getPurpose()).isEqualTo(newPurpose);
        assertThat(circle.getAccountabilities()).containsExactlyElementsOf(newAccountabilities);
    }
    
    @Test
    void shouldAddAccountability() {
        // Given
        Circle circle = Circle.createAnchorCircle(OrganizationId.generate());
        String accountability = "Manage resources";
        
        // When
        circle.addAccountability(accountability);
        
        // Then
        assertThat(circle.getAccountabilities()).contains(accountability);
    }
    
    @Test
    void shouldThrowExceptionWhenAddingNullAccountability() {
        // Given
        Circle circle = Circle.createAnchorCircle(OrganizationId.generate());
        
        // When & Then
        assertThatThrownBy(() -> circle.addAccountability(null))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("accountability");
    }
    
    @Test
    void shouldAddSubCircle() {
        // Given
        Circle parentCircle = Circle.createAnchorCircle(OrganizationId.generate());
        Circle subCircle = Circle.createSubCircle(
            "Sub Circle",
            "Sub purpose",
            parentCircle.getId(),
            parentCircle.getOrganizationId()
        );
        
        // When
        parentCircle.addSubCircle(subCircle);
        
        // Then
        assertThat(parentCircle.getSubCircles()).contains(subCircle);
    }
    
    @Test
    void shouldThrowExceptionWhenAddingSubCircleWithMismatchedParentId() {
        // Given
        Circle parentCircle = Circle.createAnchorCircle(OrganizationId.generate());
        Circle subCircle = Circle.createSubCircle(
            "Sub Circle",
            "Sub purpose",
            CircleId.generate(), // Different parent ID
            parentCircle.getOrganizationId()
        );
        
        // When & Then
        assertThatThrownBy(() -> parentCircle.addSubCircle(subCircle))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("parent ID must match");
    }
    
    @Test
    void shouldReturnImmutableCollections() {
        // Given
        Circle circle = Circle.createAnchorCircle(OrganizationId.generate());
        
        // When & Then
        assertThatThrownBy(() -> circle.getRoles().clear())
            .isInstanceOf(UnsupportedOperationException.class);
        
        assertThatThrownBy(() -> circle.getSubCircles().clear())
            .isInstanceOf(UnsupportedOperationException.class);
        
        assertThatThrownBy(() -> circle.getAccountabilities().clear())
            .isInstanceOf(UnsupportedOperationException.class);
    }
    
    @Test
    void shouldBeEqualWhenIdsAreEqual() {
        // Given
        Circle circle1 = Circle.createAnchorCircle(OrganizationId.generate());
        Circle circle2 = Circle.createAnchorCircle(OrganizationId.generate());
        
        // When & Then
        assertThat(circle1).isEqualTo(circle1);
        assertThat(circle1).isNotEqualTo(circle2);
    }
    
    @Test
    void shouldHaveConsistentHashCode() {
        // Given
        Circle circle = Circle.createAnchorCircle(OrganizationId.generate());
        
        // When
        int hashCode1 = circle.hashCode();
        int hashCode2 = circle.hashCode();
        
        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }
    
    @Test
    void shouldHaveReadableToString() {
        // Given
        Circle circle = Circle.createAnchorCircle(OrganizationId.generate());
        
        // When
        String result = circle.toString();
        
        // Then
        assertThat(result).contains("Circle");
        assertThat(result).contains("Anchor Circle");
        assertThat(result).contains("isAnchorCircle=true");
    }
    
    @Test
    void shouldIdentifySpecialRolesByType() {
        // Given
        Circle circle = Circle.createAnchorCircle(OrganizationId.generate());
        
        // When
        long circleLeadCount = circle.getRoles().stream()
            .filter(r -> r.getSpecialRoleType() == SpecialRoleType.CIRCLE_LEAD)
            .count();
        long facilitatorCount = circle.getRoles().stream()
            .filter(r -> r.getSpecialRoleType() == SpecialRoleType.FACILITATOR)
            .count();
        long secretaryCount = circle.getRoles().stream()
            .filter(r -> r.getSpecialRoleType() == SpecialRoleType.SECRETARY)
            .count();
        long circleRepCount = circle.getRoles().stream()
            .filter(r -> r.getSpecialRoleType() == SpecialRoleType.CIRCLE_REP)
            .count();
        
        // Then
        assertThat(circleLeadCount).isEqualTo(1);
        assertThat(facilitatorCount).isEqualTo(1);
        assertThat(secretaryCount).isEqualTo(1);
        assertThat(circleRepCount).isEqualTo(1);
    }
}
