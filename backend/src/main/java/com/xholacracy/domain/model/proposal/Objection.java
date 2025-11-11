package com.xholacracy.domain.model.proposal;

import com.xholacracy.domain.model.partner.PartnerId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Objection 实体 - 反对
 * 在反对阶段，参与者提出的反对意见
 */
@Entity
@Table(name = "objections")
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Objection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "objector_id"))
    })
    private PartnerId objectorId;
    
    @Column(name = "reasoning", nullable = false, length = 2000)
    private String reasoning;
    
    @Column(name = "is_valid")
    private Boolean isValid;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "validated_by"))
    })
    private PartnerId validatedBy;
    
    @Embedded
    private ObjectionCriteria criteria;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    /**
     * 创建反对
     */
    public static Objection create(PartnerId objectorId, String reasoning, ObjectionCriteria criteria) {
        if (objectorId == null) {
            throw new IllegalArgumentException("Objector ID cannot be null");
        }
        if (reasoning == null || reasoning.trim().isEmpty()) {
            throw new IllegalArgumentException("Objection reasoning cannot be null or empty");
        }
        if (criteria == null) {
            throw new IllegalArgumentException("Objection criteria cannot be null");
        }
        
        return new Objection(null, objectorId, reasoning, null, null, criteria, LocalDateTime.now());
    }
    
    /**
     * 验证反对（由协调员执行）
     */
    public void validate(PartnerId facilitatorId, boolean isValid) {
        if (facilitatorId == null) {
            throw new IllegalArgumentException("Facilitator ID cannot be null");
        }
        if (this.isValid != null) {
            throw new IllegalStateException("Objection has already been validated");
        }
        
        this.isValid = isValid;
        this.validatedBy = facilitatorId;
    }
    
    /**
     * 检查反对是否有效
     */
    public boolean isValid() {
        return Boolean.TRUE.equals(this.isValid);
    }
}
