package com.xholacracy.domain.model.proposal;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Tension 值对象 - 表示现状与期望之间的差距
 * 紧张是推动组织变革和改进的核心概念
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tension {
    
    @Column(name = "tension_description", nullable = false, length = 1000)
    private String description;
    
    @Column(name = "tension_current_state", nullable = false, length = 500)
    private String currentState;
    
    @Column(name = "tension_desired_state", nullable = false, length = 500)
    private String desiredState;
    
    @ElementCollection
    @CollectionTable(name = "tension_examples", joinColumns = @JoinColumn(name = "proposal_id"))
    @Column(name = "example", length = 500)
    private List<String> examples = new ArrayList<>();
    
    @Column(name = "tension_context", length = 1000)
    private String context;
    
    /**
     * 创建 Tension 值对象
     */
    public static Tension create(String description, String currentState, String desiredState) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Tension description cannot be null or empty");
        }
        if (currentState == null || currentState.trim().isEmpty()) {
            throw new IllegalArgumentException("Current state cannot be null or empty");
        }
        if (desiredState == null || desiredState.trim().isEmpty()) {
            throw new IllegalArgumentException("Desired state cannot be null or empty");
        }
        
        return new Tension(description, currentState, desiredState, new ArrayList<>(), null);
    }
    
    /**
     * 添加示例
     */
    public Tension withExample(String example) {
        if (example != null && !example.trim().isEmpty()) {
            List<String> newExamples = new ArrayList<>(this.examples);
            newExamples.add(example);
            return new Tension(this.description, this.currentState, this.desiredState, newExamples, this.context);
        }
        return this;
    }
    
    /**
     * 添加上下文
     */
    public Tension withContext(String context) {
        return new Tension(this.description, this.currentState, this.desiredState, this.examples, context);
    }
}
