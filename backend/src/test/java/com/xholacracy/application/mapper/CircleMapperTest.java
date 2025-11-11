package com.xholacracy.application.mapper;

import com.xholacracy.application.dto.circle.CircleDTO;
import com.xholacracy.application.dto.circle.CircleSummaryDTO;
import com.xholacracy.application.dto.circle.CreateCircleRequest;
import com.xholacracy.application.dto.circle.UpdateCircleRequest;
import com.xholacracy.domain.model.circle.Circle;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.organization.OrganizationId;
import com.xholacracy.domain.model.role.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for CircleMapper
 */
class CircleMapperTest {
    
    private final CircleMapper mapper = Mappers.getMapper(CircleMapper.class);
    
    @Test
    void shouldMapCircleToDTO() {
        // Given
        CircleId circleId = CircleId.generate();
        OrganizationId orgId = OrganizationId.generate();
        
        Circle circle = new Circle();
        circle.setId(circleId);
        circle.setName("Product Circle");
        circle.setPurpose("Product development");
        circle.setOrganizationId(orgId);
        circle.setAccountabilities(List.of("Planning", "Development"));
        circle.setRoles(new ArrayList<>());
        circle.setSubCircles(new ArrayList<>());
        
        // When
        CircleDTO dto = mapper.toDTO(circle);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(circleId.getValue());
        assertThat(dto.getName()).isEqualTo("Product Circle");
        assertThat(dto.getPurpose()).isEqualTo("Product development");
        assertThat(dto.getOrganizationId()).isEqualTo(orgId.getValue());
        assertThat(dto.getAccountabilities()).containsExactly("Planning", "Development");
    }
    
    @Test
    void shouldMapCircleToSummaryDTO() {
        // Given
        Circle circle = new Circle();
        circle.setId(CircleId.generate());
        circle.setName("Product Circle");
        circle.setPurpose("Product development");
        
        List<Role> roles = new ArrayList<>();
        roles.add(new Role());
        roles.add(new Role());
        circle.setRoles(roles);
        
        List<Circle> subCircles = new ArrayList<>();
        subCircles.add(new Circle());
        circle.setSubCircles(subCircles);
        
        // When
        CircleSummaryDTO dto = mapper.toSummaryDTO(circle);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo("Product Circle");
        assertThat(dto.getPurpose()).isEqualTo("Product development");
        assertThat(dto.getRoleCount()).isEqualTo(2);
        assertThat(dto.getSubCircleCount()).isEqualTo(1);
    }
    
    @Test
    void shouldMapCreateRequestToEntity() {
        // Given
        CreateCircleRequest request = CreateCircleRequest.builder()
            .name("New Circle")
            .purpose("New Purpose")
            .accountabilities(List.of("Task 1", "Task 2"))
            .organizationId("org-123")
            .parentCircleId("parent-456")
            .build();
        
        // When
        Circle circle = mapper.toEntity(request);
        
        // Then
        assertThat(circle).isNotNull();
        assertThat(circle.getName()).isEqualTo("New Circle");
        assertThat(circle.getPurpose()).isEqualTo("New Purpose");
        assertThat(circle.getAccountabilities()).containsExactly("Task 1", "Task 2");
    }
    
    @Test
    void shouldUpdateEntityFromRequest() {
        // Given
        Circle circle = new Circle();
        circle.setId(CircleId.generate());
        circle.setName("Old Name");
        circle.setPurpose("Old Purpose");
        
        UpdateCircleRequest request = UpdateCircleRequest.builder()
            .name("Updated Name")
            .purpose("Updated Purpose")
            .accountabilities(List.of("New Task"))
            .build();
        
        // When
        mapper.updateEntity(request, circle);
        
        // Then
        assertThat(circle.getName()).isEqualTo("Updated Name");
        assertThat(circle.getPurpose()).isEqualTo("Updated Purpose");
        assertThat(circle.getAccountabilities()).containsExactly("New Task");
        assertThat(circle.getId()).isNotNull(); // ID should not change
    }
    
    @Test
    void shouldMapCircleIdStringToCircleId() {
        // Given
        String idString = "circle-123";
        
        // When
        CircleId id = mapper.mapId(idString);
        
        // Then
        assertThat(id).isNotNull();
        assertThat(id.getValue()).isEqualTo(idString);
    }
    
    @Test
    void shouldHandleNullParentCircleId() {
        // Given
        Circle circle = new Circle();
        circle.setId(CircleId.generate());
        circle.setName("Anchor Circle");
        circle.setOrganizationId(OrganizationId.generate());
        circle.setParentCircleId(null);
        
        // When
        CircleDTO dto = mapper.toDTO(circle);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getParentCircleId()).isNull();
    }
}
