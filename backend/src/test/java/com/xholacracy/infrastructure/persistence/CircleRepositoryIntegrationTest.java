package com.xholacracy.infrastructure.persistence;

import com.xholacracy.domain.model.circle.Circle;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.circle.CircleRepository;
import com.xholacracy.domain.model.organization.Organization;
import com.xholacracy.domain.model.organization.OrganizationId;
import com.xholacracy.domain.model.organization.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Circle Repository集成测试
 * 测试Repository的持久化功能
 */
@DataJpaTest
@ComponentScan(basePackages = "com.xholacracy.infrastructure.persistence")
@ActiveProfiles("test")
class CircleRepositoryIntegrationTest {
    
    @Autowired
    private CircleRepository circleRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    private OrganizationId organizationId;
    private CircleId anchorCircleId;
    
    @BeforeEach
    void setUp() {
        // Create an organization with anchor circle
        Organization organization = Organization.create("Test Org", "Description");
        Organization saved = organizationRepository.save(organization);
        organizationId = saved.getId();
        anchorCircleId = saved.getAnchorCircleId();
    }
    
    @Test
    void shouldSaveAndFindCircle() {
        // Given
        Circle subCircle = Circle.createSubCircle(
            "Product Circle",
            "Product development",
            anchorCircleId,
            organizationId
        );
        
        // When
        Circle saved = circleRepository.save(subCircle);
        Optional<Circle> found = circleRepository.findById(saved.getId());
        
        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Product Circle");
        assertThat(found.get().getPurpose()).isEqualTo("Product development");
        assertThat(found.get().getRoles()).hasSize(4); // 4 special roles
    }
    
    @Test
    void shouldFindCirclesByOrganizationId() {
        // Given
        Circle subCircle1 = Circle.createSubCircle(
            "Circle 1",
            "Purpose 1",
            anchorCircleId,
            organizationId
        );
        Circle subCircle2 = Circle.createSubCircle(
            "Circle 2",
            "Purpose 2",
            anchorCircleId,
            organizationId
        );
        circleRepository.save(subCircle1);
        circleRepository.save(subCircle2);
        
        // When
        List<Circle> circles = circleRepository.findByOrganizationId(organizationId);
        
        // Then
        assertThat(circles).hasSizeGreaterThanOrEqualTo(3); // Anchor + 2 sub-circles
    }
    
    @Test
    void shouldFindCirclesByParentCircleId() {
        // Given
        Circle subCircle1 = Circle.createSubCircle(
            "Sub Circle 1",
            "Purpose 1",
            anchorCircleId,
            organizationId
        );
        Circle subCircle2 = Circle.createSubCircle(
            "Sub Circle 2",
            "Purpose 2",
            anchorCircleId,
            organizationId
        );
        circleRepository.save(subCircle1);
        circleRepository.save(subCircle2);
        
        // When
        List<Circle> subCircles = circleRepository.findByParentCircleId(anchorCircleId);
        
        // Then
        assertThat(subCircles).hasSize(2);
    }
    
    @Test
    void shouldFindAnchorCircle() {
        // When
        Optional<Circle> anchorCircle = circleRepository.findAnchorCircleByOrganizationId(organizationId);
        
        // Then
        assertThat(anchorCircle).isPresent();
        assertThat(anchorCircle.get().getName()).isEqualTo("Anchor Circle");
        assertThat(anchorCircle.get().isAnchorCircle()).isTrue();
    }
    
    @Test
    void shouldDeleteCircle() {
        // Given
        Circle subCircle = Circle.createSubCircle(
            "To Delete",
            "Purpose",
            anchorCircleId,
            organizationId
        );
        Circle saved = circleRepository.save(subCircle);
        
        // When
        circleRepository.deleteById(saved.getId());
        Optional<Circle> found = circleRepository.findById(saved.getId());
        
        // Then
        assertThat(found).isEmpty();
    }
    
    @Test
    void shouldUpdateCircle() {
        // Given
        Circle circle = Circle.createSubCircle(
            "Original Name",
            "Original Purpose",
            anchorCircleId,
            organizationId
        );
        Circle saved = circleRepository.save(circle);
        
        // When
        saved.updateInfo("Updated Name", "Updated Purpose", List.of("Accountability 1", "Accountability 2"));
        circleRepository.save(saved);
        Optional<Circle> found = circleRepository.findById(saved.getId());
        
        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Updated Name");
        assertThat(found.get().getPurpose()).isEqualTo("Updated Purpose");
        assertThat(found.get().getAccountabilities()).hasSize(2);
    }
}
