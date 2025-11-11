package com.xholacracy.application.mapper;

import com.xholacracy.application.dto.circle.CircleDTO;
import com.xholacracy.application.dto.circle.CircleSummaryDTO;
import com.xholacracy.application.dto.circle.CreateCircleRequest;
import com.xholacracy.application.dto.circle.UpdateCircleRequest;
import com.xholacracy.domain.model.circle.Circle;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.organization.OrganizationId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * MapStruct mapper for Circle entity and DTOs
 */
@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface CircleMapper {
    
    @Mapping(target = "id", expression = "java(circle.getId().getValue())")
    @Mapping(target = "parentCircleId", expression = "java(circle.getParentCircleId() != null ? circle.getParentCircleId().getValue() : null)")
    @Mapping(target = "organizationId", expression = "java(circle.getOrganizationId().getValue())")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "subCircles", source = "subCircles")
    CircleDTO toDTO(Circle circle);
    
    @Mapping(target = "id", expression = "java(circle.getId().getValue())")
    @Mapping(target = "roleCount", expression = "java(circle.getRoles() != null ? circle.getRoles().size() : 0)")
    @Mapping(target = "subCircleCount", expression = "java(circle.getSubCircles() != null ? circle.getSubCircles().size() : 0)")
    CircleSummaryDTO toSummaryDTO(Circle circle);
    
    List<CircleSummaryDTO> toSummaryDTOList(List<Circle> circles);
    
    // Note: Use Circle.createSubCircle() or Circle.createAnchorCircle() factory methods
    // Entities should be created through domain factory methods to ensure business rules
    
    void updateFromRequest(UpdateCircleRequest request, @MappingTarget Circle circle);
    
    default CircleId mapId(String id) {
        return id != null ? CircleId.of(id) : null;
    }
    
    default String mapId(CircleId id) {
        return id != null ? id.getValue() : null;
    }
    
    default OrganizationId mapOrganizationId(String id) {
        return id != null ? OrganizationId.of(id) : null;
    }
}
