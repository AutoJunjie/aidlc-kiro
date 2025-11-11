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
 * Vote 值对象 - 投票
 * 审批者对提案的投票
 */
@Entity
@Table(name = "votes")
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Vote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "voter_id"))
    })
    private PartnerId voterId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", nullable = false)
    private VoteType voteType;
    
    @Column(name = "comment", length = 1000)
    private String comment;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    /**
     * 创建投票
     */
    public static Vote create(PartnerId voterId, VoteType voteType, String comment) {
        if (voterId == null) {
            throw new IllegalArgumentException("Voter ID cannot be null");
        }
        if (voteType == null) {
            throw new IllegalArgumentException("Vote type cannot be null");
        }
        
        return new Vote(null, voterId, voteType, comment, LocalDateTime.now());
    }
    
    /**
     * 创建投票（无评论）
     */
    public static Vote create(PartnerId voterId, VoteType voteType) {
        return create(voterId, voteType, null);
    }
}
