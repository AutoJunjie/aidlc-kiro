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
 * Reaction 值对象 - 反应
 * 在反应阶段，参与者对提案的反应
 */
@Entity
@Table(name = "reactions")
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "reactor_id"))
    })
    private PartnerId reactorId;
    
    @Column(name = "content", nullable = false, length = 2000)
    private String content;
    
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    /**
     * 创建反应
     */
    public static Reaction create(PartnerId reactorId, String content, int orderIndex) {
        if (reactorId == null) {
            throw new IllegalArgumentException("Reactor ID cannot be null");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Reaction content cannot be null or empty");
        }
        if (orderIndex < 0) {
            throw new IllegalArgumentException("Order index must be non-negative");
        }
        
        return new Reaction(null, reactorId, content, orderIndex, LocalDateTime.now());
    }
}
