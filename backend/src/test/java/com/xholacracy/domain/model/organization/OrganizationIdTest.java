package com.xholacracy.domain.model.organization;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * OrganizationId值对象单元测试
 * 验证值对象的不可变性和相等性
 */
class OrganizationIdTest {
    
    @Test
    void shouldGenerateUniqueIds() {
        // When
        OrganizationId id1 = OrganizationId.generate();
        OrganizationId id2 = OrganizationId.generate();
        
        // Then
        assertThat(id1).isNotNull();
        assertThat(id2).isNotNull();
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id1.getValue()).isNotEmpty();
        assertThat(id2.getValue()).isNotEmpty();
    }
    
    @Test
    void shouldCreateFromValue() {
        // Given
        String value = "org-123";
        
        // When
        OrganizationId id = OrganizationId.of(value);
        
        // Then
        assertThat(id).isNotNull();
        assertThat(id.getValue()).isEqualTo(value);
    }
    
    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        // When & Then
        assertThatThrownBy(() -> OrganizationId.of(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or empty");
    }
    
    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
        // When & Then
        assertThatThrownBy(() -> OrganizationId.of(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or empty");
        
        assertThatThrownBy(() -> OrganizationId.of("   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or empty");
    }
    
    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        // Given
        String value = "org-123";
        OrganizationId id1 = OrganizationId.of(value);
        OrganizationId id2 = OrganizationId.of(value);
        
        // Then
        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }
    
    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        // Given
        OrganizationId id1 = OrganizationId.of("org-123");
        OrganizationId id2 = OrganizationId.of("org-456");
        
        // Then
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id1.hashCode()).isNotEqualTo(id2.hashCode());
    }
    
    @Test
    void shouldReturnValueAsString() {
        // Given
        String value = "org-123";
        OrganizationId id = OrganizationId.of(value);
        
        // When
        String result = id.toString();
        
        // Then
        assertThat(result).isEqualTo(value);
    }
}
