package com.xholacracy.domain.model.proposal;

import com.xholacracy.domain.model.partner.PartnerId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegrativeDecisionValueObjectsTest {
    
    // Question Tests
    
    @Test
    void shouldCreateQuestion() {
        // Given
        PartnerId askerId = PartnerId.generate();
        String questionText = "Can you clarify the scope?";
        
        // When
        Question question = Question.create(askerId, questionText);
        
        // Then
        assertNotNull(question);
        assertEquals(askerId, question.getAskerId());
        assertEquals(questionText, question.getQuestion());
        assertNull(question.getAnswer());
        assertNotNull(question.getTimestamp());
    }
    
    @Test
    void shouldThrowExceptionWhenQuestionTextIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Question.create(PartnerId.generate(), null)
        );
    }
    
    @Test
    void shouldAddAnswerToQuestion() {
        // Given
        Question question = Question.create(PartnerId.generate(), "Question?");
        String answer = "This is the answer";
        
        // When
        Question answeredQuestion = question.withAnswer(answer);
        
        // Then
        assertEquals(answer, answeredQuestion.getAnswer());
    }
    
    // Reaction Tests
    
    @Test
    void shouldCreateReaction() {
        // Given
        PartnerId reactorId = PartnerId.generate();
        String content = "I think this is a good idea";
        int orderIndex = 1;
        
        // When
        Reaction reaction = Reaction.create(reactorId, content, orderIndex);
        
        // Then
        assertNotNull(reaction);
        assertEquals(reactorId, reaction.getReactorId());
        assertEquals(content, reaction.getContent());
        assertEquals(orderIndex, reaction.getOrderIndex());
        assertNotNull(reaction.getTimestamp());
    }
    
    @Test
    void shouldThrowExceptionWhenReactionContentIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Reaction.create(PartnerId.generate(), null, 0)
        );
    }
    
    @Test
    void shouldThrowExceptionWhenOrderIndexIsNegative() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Reaction.create(PartnerId.generate(), "content", -1)
        );
    }
    
    // Amendment Tests
    
    @Test
    void shouldCreateAmendment() {
        // Given
        String description = "Changed the role name";
        String reason = "Based on feedback";
        
        // When
        Amendment amendment = Amendment.create(description, reason);
        
        // Then
        assertNotNull(amendment);
        assertEquals(description, amendment.getDescription());
        assertEquals(reason, amendment.getReason());
        assertNotNull(amendment.getTimestamp());
    }
    
    @Test
    void shouldCreateAmendmentWithoutReason() {
        // Given
        String description = "Changed the role name";
        
        // When
        Amendment amendment = Amendment.create(description, null);
        
        // Then
        assertNotNull(amendment);
        assertEquals(description, amendment.getDescription());
        assertNull(amendment.getReason());
    }
    
    @Test
    void shouldThrowExceptionWhenAmendmentDescriptionIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Amendment.create(null, "reason")
        );
    }
    
    // ObjectionCriteria Tests
    
    @Test
    void shouldCreateObjectionCriteria() {
        // When
        ObjectionCriteria criteria = ObjectionCriteria.create(true, false, false, false);
        
        // Then
        assertNotNull(criteria);
        assertTrue(criteria.getReducesCapability());
        assertFalse(criteria.getLimitsAccountability());
        assertFalse(criteria.getProblemNotExistWithout());
        assertFalse(criteria.getCausesHarm());
    }
    
    @Test
    void shouldBeValidWhenAnyCriteriaIsTrue() {
        // Given
        ObjectionCriteria criteria1 = ObjectionCriteria.create(true, false, false, false);
        ObjectionCriteria criteria2 = ObjectionCriteria.create(false, true, false, false);
        ObjectionCriteria criteria3 = ObjectionCriteria.create(false, false, true, false);
        ObjectionCriteria criteria4 = ObjectionCriteria.create(false, false, false, true);
        
        // Then
        assertTrue(criteria1.isValid());
        assertTrue(criteria2.isValid());
        assertTrue(criteria3.isValid());
        assertTrue(criteria4.isValid());
    }
    
    @Test
    void shouldBeInvalidWhenAllCriteriaAreFalse() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(false, false, false, false);
        
        // Then
        assertFalse(criteria.isValid());
    }
    
    // Objection Tests
    
    @Test
    void shouldCreateObjection() {
        // Given
        PartnerId objectorId = PartnerId.generate();
        String reasoning = "This will cause problems";
        ObjectionCriteria criteria = ObjectionCriteria.create(true, false, false, false);
        
        // When
        Objection objection = Objection.create(objectorId, reasoning, criteria);
        
        // Then
        assertNotNull(objection);
        assertEquals(objectorId, objection.getObjectorId());
        assertEquals(reasoning, objection.getReasoning());
        assertEquals(criteria, objection.getCriteria());
        assertNull(objection.getIsValid());
        assertNull(objection.getValidatedBy());
        assertNotNull(objection.getTimestamp());
    }
    
    @Test
    void shouldThrowExceptionWhenObjectionReasoningIsNull() {
        // Given
        ObjectionCriteria criteria = ObjectionCriteria.create(true, false, false, false);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Objection.create(PartnerId.generate(), null, criteria)
        );
    }
    
    @Test
    void shouldValidateObjection() {
        // Given
        Objection objection = Objection.create(
            PartnerId.generate(),
            "reasoning",
            ObjectionCriteria.create(true, false, false, false)
        );
        PartnerId facilitatorId = PartnerId.generate();
        
        // When
        objection.validate(facilitatorId, true);
        
        // Then
        assertTrue(objection.isValid());
        assertEquals(facilitatorId, objection.getValidatedBy());
    }
    
    @Test
    void shouldThrowExceptionWhenValidatingTwice() {
        // Given
        Objection objection = Objection.create(
            PartnerId.generate(),
            "reasoning",
            ObjectionCriteria.create(true, false, false, false)
        );
        PartnerId facilitatorId = PartnerId.generate();
        objection.validate(facilitatorId, true);
        
        // When & Then
        assertThrows(IllegalStateException.class, () ->
            objection.validate(facilitatorId, false)
        );
    }
    
    // Vote Tests
    
    @Test
    void shouldCreateVote() {
        // Given
        PartnerId voterId = PartnerId.generate();
        VoteType voteType = VoteType.APPROVE;
        String comment = "I support this proposal";
        
        // When
        Vote vote = Vote.create(voterId, voteType, comment);
        
        // Then
        assertNotNull(vote);
        assertEquals(voterId, vote.getVoterId());
        assertEquals(voteType, vote.getVoteType());
        assertEquals(comment, vote.getComment());
        assertNotNull(vote.getTimestamp());
    }
    
    @Test
    void shouldCreateVoteWithoutComment() {
        // Given
        PartnerId voterId = PartnerId.generate();
        VoteType voteType = VoteType.APPROVE;
        
        // When
        Vote vote = Vote.create(voterId, voteType);
        
        // Then
        assertNotNull(vote);
        assertEquals(voterId, vote.getVoterId());
        assertEquals(voteType, vote.getVoteType());
        assertNull(vote.getComment());
    }
    
    @Test
    void shouldThrowExceptionWhenVoteTypeIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            Vote.create(PartnerId.generate(), null, "comment")
        );
    }
}
