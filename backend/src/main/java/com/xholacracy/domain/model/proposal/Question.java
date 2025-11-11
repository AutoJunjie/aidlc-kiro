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
 * Question 值对象 - 澄清问题
 * 在澄清阶段，参与者提出的问题
 */
@Entity
@Table(name = "questions")
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "asker_id"))
    })
    private PartnerId askerId;
    
    @Column(name = "question", nullable = false, length = 1000)
    private String question;
    
    @Column(name = "answer", length = 1000)
    private String answer;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    /**
     * 创建澄清问题
     */
    public static Question create(PartnerId askerId, String question) {
        if (askerId == null) {
            throw new IllegalArgumentException("Asker ID cannot be null");
        }
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("Question cannot be null or empty");
        }
        
        return new Question(null, askerId, question, null, LocalDateTime.now());
    }
    
    /**
     * 回答问题
     */
    public Question withAnswer(String answer) {
        return new Question(this.id, this.askerId, this.question, answer, this.timestamp);
    }
}
