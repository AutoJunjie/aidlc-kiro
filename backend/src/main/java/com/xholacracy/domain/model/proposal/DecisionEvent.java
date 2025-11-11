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
 * DecisionEvent 值对象 - 记录提案决策过程中的事件
 */
@Entity
@Table(name = "decision_events")
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DecisionEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private DecisionEventType eventType;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "actor_id"))
    })
    private PartnerId actorId;
    
    @Column(name = "content", length = 1000)
    private String content;
    
    /**
     * 创建决策事件
     */
    public static DecisionEvent create(DecisionEventType eventType, PartnerId actorId, String content) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }
        
        return new DecisionEvent(null, eventType, LocalDateTime.now(), actorId, content);
    }
}
