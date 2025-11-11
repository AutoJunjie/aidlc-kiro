package com.xholacracy.infrastructure.persistence;

import com.xholacracy.domain.model.organization.Organization;
import com.xholacracy.domain.model.organization.OrganizationId;
import com.xholacracy.domain.model.organization.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Organization Repository集成测试
 * 测试Repository的持久化功能
 */
@DataJpaTest
@ComponentScan(basePackages = "com.xholacracy.infrastructure.persistence")
@ActiveProfiles("test")
class OrganizationRepositoryIntegrationTest {
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Test
    void shouldSaveAndFindOrganization() {
        // Given
        Organization organization = Organization.create(
            "Test Organization",
            "A test organization"
        );
        
        // When
        Organization saved = organizationRepository.save(organization);
        Optional<Organization> found = organizationRepository.findById(saved.getId());
        
        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Organization");
        assertThat(found.get().getDescription()).isEqualTo("A test organization");
        assertThat(found.get().getAnchorCircle()).isNotNull();
    }
    
    @Test
    void shouldFindOrganizationByName() {
        // Given
        Organization organization = Organization.create(
            "Unique Org Name",
            "Description"
        );
        organizationRepository.save(organization);
        
        // When
        Optional<Organization> found = organizationRepository.findByName("Unique Org Name");
        
        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Unique Org Name");
    }
    
    @Test
    void shouldFindAllOrganizations() {
        // Given
        Organization org1 = Organization.create("Org 1", "Description 1");
        Organization org2 = Organization.create("Org 2", "Description 2");
        organizationRepository.save(org1);
        organizationRepository.save(org2);
        
        // When
        List<Organization> all = organizationRepository.findAll();
        
        // Then
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
    }
    
    @Test
    void shouldCheckIfOrganizationExists() {
        // Given
        Organization organization = Organization.create("Test Org", "Description");
        Organization saved = organizationRepository.save(organization);
        
        // When
        boolean exists = organizationRepository.existsById(saved.getId());
        boolean notExists = organizationRepository.existsById(OrganizationId.generate());
        
        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
    
    @Test
    void shouldDeleteOrganization() {
        // Given
        Organization organization = Organization.create("To Delete", "Description");
        Organization saved = organizationRepository.save(organization);
        
        // When
        organizationRepository.deleteById(saved.getId());
        Optional<Organization> found = organizationRepository.findById(saved.getId());
        
        // Then
        assertThat(found).isEmpty();
    }
    
    @Test
    void shouldUpdateOrganization() {
        // Given
        Organization organization = Organization.create("Original Name", "Original Description");
        Organization saved = organizationRepository.save(organization);
        
        // When
        saved.updateInfo("Updated Name", "Updated Description");
        organizationRepository.save(saved);
        Optional<Organization> found = organizationRepository.findById(saved.getId());
        
        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Updated Name");
        assertThat(found.get().getDescription()).isEqualTo("Updated Description");
    }
}
