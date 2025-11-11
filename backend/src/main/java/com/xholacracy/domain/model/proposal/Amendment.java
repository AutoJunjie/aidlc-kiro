package com.xholacracy.domain.model.proposal;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Amendment 值对象 - 修改
 * 在修改阶段，提案人对提案的修改
 */
@Entity
@Table(name = "amendments")
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Amendment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "description", nullable = false, length = 2000)
    private String description;
    
    @Column(name = "reason", length = 1000)
    private String reason;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    /**
     * 创建修改
     */
    public static Amendment create(String description, String reason) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Amendment description cannot be null or empty");
        }
        
        return new Amendment(null, description, reason, LocalDateTime.now());
    }
}
