package com.xholacracy.domain.model.circle;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * CircleId值对象单元测试
 * 验证值对象的不可变性和相等性
 */
class CircleIdTest {
    
    @Test
    void shouldGenerateUniqueIds() {
        // When
        CircleId id1 = CircleId.generate();
        CircleId id2 = CircleId.generate();
        
        // Then
        assertThat(id1).isNotNull();
        assertThat(id2).isNotNull();
        assertThat(id1).isNotEqualTo(id2);
    }
    
    @Test
    void shouldCreateFromValue() {
        // Given
        String value = "circle-123";
        
        // When
        CircleId id = CircleId.of(value);
        
        // Then
        assertThat(id.getValue()).isEqualTo(value);
    }
    
    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        // When & Then
        assertThatThrownBy(() -> CircleId.of(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or empty");
    }
    
    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        // Given
        String value = "circle-123";
        CircleId id1 = CircleId.of(value);
        CircleId id2 = CircleId.of(value);
        
        // Then
        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }
}
