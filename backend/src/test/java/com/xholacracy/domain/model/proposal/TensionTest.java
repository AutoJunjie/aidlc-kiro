package com.xholacracy.domain.model.proposal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TensionTest {
    
    @Test
    void shouldCreateTensionWithValidData() {
        // Given
        String description = "Need better communication";
        String currentState = "Communication is slow and unclear";
        String desiredState = "Fast and clear communication";
        
        // When
        Tension tension = Tension.create(description, currentState, desiredState);
        
        // Then
        assertNotNull(tension);
        assertEquals(description, tension.getDescription());
        assertEquals(currentState, tension.getCurrentState());
        assertEquals(desiredState, tension.getDesiredState());
        assertTrue(tension.getExamples().isEmpty());
        assertNull(tension.getContext());
    }
    
    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            Tension.create(null, "current", "desired")
        );
    }
    
    @Test
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            Tension.create("  ", "current", "desired")
        );
    }
    
    @Test
    void shouldThrowExceptionWhenCurrentStateIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            Tension.create("description", null, "desired")
        );
    }
    
    @Test
    void shouldThrowExceptionWhenDesiredStateIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            Tension.create("description", "current", null)
        );
    }
    
    @Test
    void shouldAddExample() {
        // Given
        Tension tension = Tension.create("description", "current", "desired");
        String example = "Last week we missed a deadline due to poor communication";
        
        // When
        Tension updatedTension = tension.withExample(example);
        
        // Then
        assertEquals(1, updatedTension.getExamples().size());
        assertEquals(example, updatedTension.getExamples().get(0));
        // Original tension should be unchanged (immutability)
        assertTrue(tension.getExamples().isEmpty());
    }
    
    @Test
    void shouldAddMultipleExamples() {
        // Given
        Tension tension = Tension.create("description", "current", "desired");
        
        // When
        Tension updated = tension
            .withExample("Example 1")
            .withExample("Example 2")
            .withExample("Example 3");
        
        // Then
        assertEquals(3, updated.getExamples().size());
    }
    
    @Test
    void shouldNotAddEmptyExample() {
        // Given
        Tension tension = Tension.create("description", "current", "desired");
        
        // When
        Tension updated = tension.withExample("  ");
        
        // Then
        assertTrue(updated.getExamples().isEmpty());
    }
    
    @Test
    void shouldAddContext() {
        // Given
        Tension tension = Tension.create("description", "current", "desired");
        String context = "This affects the entire marketing team";
        
        // When
        Tension updatedTension = tension.withContext(context);
        
        // Then
        assertEquals(context, updatedTension.getContext());
        // Original tension should be unchanged
        assertNull(tension.getContext());
    }
    
    @Test
    void shouldBeEqualWhenAllFieldsMatch() {
        // Given
        Tension tension1 = Tension.create("desc", "current", "desired")
            .withExample("example")
            .withContext("context");
        Tension tension2 = Tension.create("desc", "current", "desired")
            .withExample("example")
            .withContext("context");
        
        // Then
        assertEquals(tension1, tension2);
        assertEquals(tension1.hashCode(), tension2.hashCode());
    }
    
    @Test
    void shouldNotBeEqualWhenFieldsDiffer() {
        // Given
        Tension tension1 = Tension.create("desc1", "current", "desired");
        Tension tension2 = Tension.create("desc2", "current", "desired");
        
        // Then
        assertNotEquals(tension1, tension2);
    }
}
