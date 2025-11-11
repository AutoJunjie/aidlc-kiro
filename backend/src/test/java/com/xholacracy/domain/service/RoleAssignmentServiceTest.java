package com.xholacracy.domain.service;

import com.xholacracy.domain.exception.DomainException;
import com.xholacracy.domain.exception.ResourceNotFoundException;
import com.xholacracy.domain.model.circle.Circle;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.circle.CircleRepository;
import com.xholacracy.domain.model.circle.SpecialRoleType;
import com.xholacracy.domain.model.organization.OrganizationId;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.role.Role;
import com.xholacracy.domain.model.role.RoleAssignment;
import com.xholacracy.domain.model.role.RoleId;
import com.xholacracy.domain.model.role.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleAssignmentServiceTest {
    
    @Mock
    private RoleRepository roleRepository;
    
    @Mock
    private CircleRepository circleRepository;
    
    private RoleAssignmentService service;
    
    @BeforeEach
    void setUp() {
        service = new RoleAssignmentService(roleRepository, circleRepository);
    }
    
    @Test
    void shouldAssignRoleToPartner() {
        // Given
        CircleId circleId = CircleId.generate();
        Role role = Role.create("Product Manager", "Manage product", circleId);
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        
        // When
        RoleAssignment assignment = service.assignRole(role.getId(), partnerId, assignedBy);
        
        // Then
        assertThat(assignment).isNotNull();
        assertThat(assignment.getPartnerId()).isEqualTo(partnerId);
        assertThat(assignment.getAssignedBy()).isEqualTo(assignedBy);
        verify(roleRepository).save(role);
    }
    
    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        // Given
        RoleId roleId = RoleId.generate();
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> service.assignRole(roleId, partnerId, assignedBy))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Role");
    }
    
    @Test
    void shouldThrowExceptionWhenRoleAlreadyAssignedToPartner() {
        // Given
        CircleId circleId = CircleId.generate();
        Role role = Role.create("Product Manager", "Manage product", circleId);
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        
        role.assignToPartner(partnerId, assignedBy);
        
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        
        // When & Then
        assertThatThrownBy(() -> service.assignRole(role.getId(), partnerId, assignedBy))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("already assigned");
    }
    
    @Test
    void shouldThrowExceptionWhenSpecialRoleAlreadyAssigned() {
        // Given
        CircleId circleId = CircleId.generate();
        Role role = Role.createSpecialRole("Circle Lead", circleId, SpecialRoleType.CIRCLE_LEAD);
        PartnerId firstPartner = PartnerId.generate();
        PartnerId secondPartner = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        
        role.assignToPartner(firstPartner, assignedBy);
        
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        
        // When & Then
        assertThatThrownBy(() -> service.assignRole(role.getId(), secondPartner, assignedBy))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Special role")
            .hasMessageContaining("can only be assigned to one partner");
    }
    
    @Test
    void shouldAutoAssignUnassignedRoleToCircleLead() {
        // Given
        CircleId circleId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        Circle circle = Circle.createSubCircle("Product Circle", "Product development", 
            CircleId.generate(), orgId);
        
        Role unassignedRole = Role.create("Product Manager", "Manage product", circle.getId());
        
        // Find Circle Lead role and assign it
        Role circleLeadRole = circle.getRoles().stream()
            .filter(r -> r.getSpecialRoleType() == SpecialRoleType.CIRCLE_LEAD)
            .findFirst()
            .orElseThrow();
        
        PartnerId circleLeadPartnerId = PartnerId.generate();
        circleLeadRole.assignToPartner(circleLeadPartnerId, circleLeadPartnerId);
        
        when(roleRepository.findById(unassignedRole.getId())).thenReturn(Optional.of(unassignedRole));
        when(circleRepository.findById(circle.getId())).thenReturn(Optional.of(circle));
        
        // When
        service.autoAssignToCircleLead(unassignedRole.getId());
        
        // Then
        assertThat(unassignedRole.getAssignments()).hasSize(1);
        assertThat(unassignedRole.getAssignments().get(0).getPartnerId()).isEqualTo(circleLeadPartnerId);
        verify(roleRepository).save(unassignedRole);
    }
    
    @Test
    void shouldNotAutoAssignIfRoleAlreadyAssigned() {
        // Given
        CircleId circleId = CircleId.generate();
        Role role = Role.create("Product Manager", "Manage product", circleId);
        PartnerId partnerId = PartnerId.generate();
        
        role.assignToPartner(partnerId, partnerId);
        
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        
        // When
        service.autoAssignToCircleLead(role.getId());
        
        // Then
        assertThat(role.getAssignments()).hasSize(1);
        verify(circleRepository, never()).findById(any());
        verify(roleRepository, never()).save(any());
    }
    
    @Test
    void shouldThrowExceptionWhenCircleNotFoundForAutoAssign() {
        // Given
        CircleId circleId = CircleId.generate();
        Role role = Role.create("Product Manager", "Manage product", circleId);
        
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        when(circleRepository.findById(circleId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> service.autoAssignToCircleLead(role.getId()))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Circle");
    }
    
    @Test
    void shouldThrowExceptionWhenCircleLeadNotAssigned() {
        // Given
        CircleId circleId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        Circle circle = Circle.createSubCircle("Product Circle", "Product development",
            CircleId.generate(), orgId);
        
        Role unassignedRole = Role.create("Product Manager", "Manage product", circle.getId());
        
        // Circle Lead is not assigned to anyone
        
        when(roleRepository.findById(unassignedRole.getId())).thenReturn(Optional.of(unassignedRole));
        when(circleRepository.findById(circle.getId())).thenReturn(Optional.of(circle));
        
        // When & Then
        assertThatThrownBy(() -> service.autoAssignToCircleLead(unassignedRole.getId()))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Circle Lead role is not assigned");
    }
    
    @Test
    void shouldRemoveAssignment() {
        // Given
        CircleId circleId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        Circle circle = Circle.createSubCircle("Product Circle", "Product development",
            CircleId.generate(), orgId);
        
        Role role = Role.create("Product Manager", "Manage product", circle.getId());
        PartnerId partnerId = PartnerId.generate();
        PartnerId assignedBy = PartnerId.generate();
        
        role.assignToPartner(partnerId, assignedBy);
        
        // Assign Circle Lead
        Role circleLeadRole = circle.getRoles().stream()
            .filter(r -> r.getSpecialRoleType() == SpecialRoleType.CIRCLE_LEAD)
            .findFirst()
            .orElseThrow();
        PartnerId circleLeadPartnerId = PartnerId.generate();
        circleLeadRole.assignToPartner(circleLeadPartnerId, circleLeadPartnerId);
        
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        when(circleRepository.findById(circle.getId())).thenReturn(Optional.of(circle));
        
        // When
        service.removeAssignment(role.getId(), partnerId);
        
        // Then
        assertThat(role.getAssignments()).hasSize(1); // Auto-assigned to Circle Lead
        assertThat(role.getAssignments().get(0).getPartnerId()).isEqualTo(circleLeadPartnerId);
        verify(roleRepository, times(2)).save(role); // Once for removal, once for auto-assign
    }
    
    @Test
    void shouldThrowExceptionWhenRemovingNonExistentAssignment() {
        // Given
        CircleId circleId = CircleId.generate();
        Role role = Role.create("Product Manager", "Manage product", circleId);
        PartnerId partnerId = PartnerId.generate();
        
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        
        // When & Then
        assertThatThrownBy(() -> service.removeAssignment(role.getId(), partnerId))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("not assigned");
    }
    
    @Test
    void shouldCheckIfRoleCanBeAssigned() {
        // Given
        CircleId circleId = CircleId.generate();
        Role role = Role.create("Product Manager", "Manage product", circleId);
        PartnerId partnerId = PartnerId.generate();
        
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        
        // When
        boolean canAssign = service.canAssignRole(role.getId(), partnerId);
        
        // Then
        assertThat(canAssign).isTrue();
    }
    
    @Test
    void shouldReturnFalseWhenRoleCannotBeAssigned() {
        // Given
        CircleId circleId = CircleId.generate();
        Role role = Role.create("Product Manager", "Manage product", circleId);
        PartnerId partnerId = PartnerId.generate();
        
        role.assignToPartner(partnerId, partnerId);
        
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        
        // When
        boolean canAssign = service.canAssignRole(role.getId(), partnerId);
        
        // Then
        assertThat(canAssign).isFalse();
    }
    
    @Test
    void shouldGetRoleAssignments() {
        // Given
        CircleId circleId = CircleId.generate();
        Role role = Role.create("Product Manager", "Manage product", circleId);
        PartnerId partnerId1 = PartnerId.generate();
        PartnerId partnerId2 = PartnerId.generate();
        
        role.assignToPartner(partnerId1, partnerId1);
        role.assignToPartner(partnerId2, partnerId2);
        
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        
        // When
        List<RoleAssignment> assignments = service.getRoleAssignments(role.getId());
        
        // Then
        assertThat(assignments).hasSize(2);
        assertThat(assignments).extracting(RoleAssignment::getPartnerId)
            .containsExactlyInAnyOrder(partnerId1, partnerId2);
    }
    
    @Test
    void shouldThrowExceptionWhenGettingAssignmentsForNonExistentRole() {
        // Given
        RoleId roleId = RoleId.generate();
        
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> service.getRoleAssignments(roleId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Role");
    }
}
