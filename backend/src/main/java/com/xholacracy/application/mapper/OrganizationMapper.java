package com.xholacracy.application.mapper;

import com.xholacracy.application.dto.organization.CreateOrganizationRequest;
import com.xholacracy.application.dto.organization.OrganizationDTO;
import com.xholacracy.application.dto.organization.UpdateOrganizationRequest;
import com.xholacracy.domain.model.organization.Organization;
import com.xholacracy.domain.model.organization.OrganizationId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Organization entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    
    @Mapping(target = "id", expression = "java(organization.getId().getValue())")
    @Mapping(target = "anchorCircleId", expression = "java(organization.getAnchorCircle() != null ? organization.getAnchorCircle().getId().getValue() : null)")
    OrganizationDTO toDTO(Organization organization);
    
    // Note: Use Organization.create() factory method instead of mapping from request
    // Entities should be created through domain factory methods to ensure business rules
    
    void updateFromRequest(UpdateOrganizationRequest request, @MappingTarget Organization organization);
    
    default OrganizationId mapId(String id) {
        return id != null ? OrganizationId.of(id) : null;
    }
    
    default String mapId(OrganizationId id) {
        return id != null ? id.getValue() : null;
    }
}
