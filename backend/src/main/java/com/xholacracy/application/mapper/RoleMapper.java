package com.xholacracy.application.mapper;

import com.xholacracy.application.dto.role.*;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.role.Domain;
import com.xholacracy.domain.model.role.Role;
import com.xholacracy.domain.model.role.RoleAssignment;
import com.xholacracy.domain.model.role.RoleId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * MapStruct mapper for Role entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {
    
    @Mapping(target = "id", expression = "java(role.getId().getValue())")
    @Mapping(target = "circleId", expression = "java(role.getCircleId().getValue())")
    @Mapping(target = "specialRoleType", expression = "java(role.getSpecialRoleType() != null ? role.getSpecialRoleType().name() : null)")
    @Mapping(target = "assignments", source = "assignments")
    RoleDTO toDTO(Role role);
    
    @Mapping(target = "id", expression = "java(role.getId().getValue())")
    @Mapping(target = "specialRoleType", expression = "java(role.getSpecialRoleType() != null ? role.getSpecialRoleType().name() : null)")
    @Mapping(target = "assignmentCount", expression = "java(role.getAssignments() != null ? role.getAssignments().size() : 0)")
    RoleSummaryDTO toSummaryDTO(Role role);
    
    List<RoleSummaryDTO> toSummaryDTOList(List<Role> roles);
    
    @Mapping(target = "id", expression = "java(assignment.getId().getValue())")
    @Mapping(target = "roleId", expression = "java(assignment.getRoleId().getValue())")
    @Mapping(target = "partnerId", expression = "java(assignment.getPartnerId().getValue())")
    @Mapping(target = "partnerName", ignore = true)
    @Mapping(target = "assignedBy", expression = "java(assignment.getAssignedBy() != null ? assignment.getAssignedBy().getValue() : null)")
    RoleAssignmentDTO toAssignmentDTO(RoleAssignment assignment);
    
    List<RoleAssignmentDTO> toAssignmentDTOList(List<RoleAssignment> assignments);
    
    @Mapping(target = "controlType", expression = "java(domain.getControlType() != null ? domain.getControlType().name() : null)")
    DomainDTO toDomainDTO(Domain domain);
    
    List<DomainDTO> toDomainDTOList(List<Domain> domains);
    
    // Note: Use Role.create() factory method instead of mapping from request
    // Entities should be created through domain factory methods to ensure business rules
    
    void updateFromRequest(UpdateRoleRequest request, @MappingTarget Role role);
    
    Domain toDomain(DomainDTO dto);
    
    List<Domain> toDomainList(List<DomainDTO> dtos);
    
    default RoleId mapId(String id) {
        return id != null ? RoleId.of(id) : null;
    }
    
    default String mapId(RoleId id) {
        return id != null ? id.getValue() : null;
    }
    
    default CircleId mapCircleId(String id) {
        return id != null ? CircleId.of(id) : null;
    }
    
    default PartnerId mapPartnerId(String id) {
        return id != null ? PartnerId.of(id) : null;
    }
}
