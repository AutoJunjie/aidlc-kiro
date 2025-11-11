package com.xholacracy.application.mapper;

import com.xholacracy.application.dto.organization.CreateOrganizationRequest;
import com.xholacracy.application.dto.organization.OrganizationDTO;
import com.xholacracy.application.dto.organization.UpdateOrganizationRequest;
import com.xholacracy.domain.model.circle.Circle;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.organization.Organization;
import com.xholacracy.domain.model.organization.OrganizationId;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for OrganizationMapper
 */
class OrganizationMapperTest {
    
    private final OrganizationMapper mapper = Mappers.getMapper(OrganizationMapper.class);
    
    @Test
    void shouldMapOrganizationToDTO() {
        // Given
        OrganizationId orgId = OrganizationId.generate();
        CircleId circleId = CircleId.generate();
        
        Organization organization = new Organization();
        organization.setId(orgId);
        organization.setName("Test Organization");
        organization.setDescription("Test Description");
        organization.setCreatedAt(LocalDateTime.now());
        organization.setUpdatedAt(LocalDateTime.now());
        
        Circle anchorCircle = new Circle();
        anchorCircle.setId(circleId);
        organization.setAnchorCircle(anchorCircle);
        
        // When
        OrganizationDTO dto = mapper.toDTO(organization);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(orgId.getValue());
        assertThat(dto.getName()).isEqualTo("Test Organization");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getAnchorCircleId()).isEqualTo(circleId.getValue());
        assertThat(dto.getCreatedAt()).isNotNull();
        assertThat(dto.getUpdatedAt()).isNotNull();
    }
    
    @Test
    void shouldMapCreateRequestToEntity() {
        // Given
        CreateOrganizationRequest request = CreateOrganizationRequest.builder()
            .name("New Organization")
            .description("New Description")
            .build();
        
        // When
        Organization organization = mapper.toEntity(request);
        
        // Then
        assertThat(organization).isNotNull();
        assertThat(organization.getName()).isEqualTo("New Organization");
        assertThat(organization.getDescription()).isEqualTo("New Description");
    }
    
    @Test
    void shouldUpdateEntityFromRequest() {
        // Given
        Organization organization = new Organization();
        organization.setId(OrganizationId.generate());
        organization.setName("Old Name");
        organization.setDescription("Old Description");
        
        UpdateOrganizationRequest request = UpdateOrganizationRequest.builder()
            .name("Updated Name")
            .description("Updated Description")
            .build();
        
        // When
        mapper.updateEntity(request, organization);
        
        // Then
        assertThat(organization.getName()).isEqualTo("Updated Name");
        assertThat(organization.getDescription()).isEqualTo("Updated Description");
        assertThat(organization.getId()).isNotNull(); // ID should not change
    }
    
    @Test
    void shouldMapIdStringToOrganizationId() {
        // Given
        String idString = "org-123";
        
        // When
        OrganizationId id = mapper.mapId(idString);
        
        // Then
        assertThat(id).isNotNull();
        assertThat(id.getValue()).isEqualTo(idString);
    }
    
    @Test
    void shouldMapOrganizationIdToString() {
        // Given
        OrganizationId id = OrganizationId.of("org-456");
        
        // When
        String idString = mapper.mapId(id);
        
        // Then
        assertThat(idString).isEqualTo("org-456");
    }
    
    @Test
    void shouldHandleNullAnchorCircle() {
        // Given
        Organization organization = new Organization();
        organization.setId(OrganizationId.generate());
        organization.setName("Test Organization");
        organization.setAnchorCircle(null);
        
        // When
        OrganizationDTO dto = mapper.toDTO(organization);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getAnchorCircleId()).isNull();
    }
}
