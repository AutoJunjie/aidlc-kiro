package com.xholacracy.application.mapper;

import com.xholacracy.application.dto.proposal.*;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ProposalMapper
 */
class ProposalMapperTest {
    
    private final ProposalMapper mapper = Mappers.getMapper(ProposalMapper.class);
    
    @Test
    void shouldMapProposalToDTO() {
        // Given
        ProposalId proposalId = ProposalId.generate();
        CircleId circleId = CircleId.generate();
        PartnerId proposerId = PartnerId.generate();
        
        Tension tension = Tension.create(
            "Need product role",
            "No dedicated role",
            "Have clear role"
        );
        
        Proposal proposal = new Proposal();
        proposal.setId(proposalId);
        proposal.setTitle("Create Product Role");
        proposal.setTension(tension);
        proposal.setProposalType(ProposalType.ROLE_MODIFICATION);
        proposal.setCircleId(circleId);
        proposal.setProposerId(proposerId);
        proposal.setStatus(ProposalStatus.DRAFT);
        proposal.setCreatedDate(LocalDateTime.now());
        proposal.setDecisionHistory(new ArrayList<>());
        proposal.setQuestions(new ArrayList<>());
        proposal.setReactions(new ArrayList<>());
        proposal.setAmendments(new ArrayList<>());
        proposal.setObjections(new ArrayList<>());
        proposal.setVotes(new ArrayList<>());
        
        // When
        ProposalDTO dto = mapper.toDTO(proposal);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(proposalId.getValue());
        assertThat(dto.getTitle()).isEqualTo("Create Product Role");
        assertThat(dto.getProposalType()).isEqualTo("ROLE_MODIFICATION");
        assertThat(dto.getStatus()).isEqualTo("DRAFT");
        assertThat(dto.getCircleId()).isEqualTo(circleId.getValue());
        assertThat(dto.getProposerId()).isEqualTo(proposerId.getValue());
        assertThat(dto.getTension()).isNotNull();
    }
    
    @Test
    void shouldMapTensionToDTO() {
        // Given
        Tension tension = Tension.create(
            "Need product role",
            "No dedicated role",
            "Have clear role"
        );
        tension.setExamples(List.of("Example 1", "Example 2"));
        tension.setContext("Additional context");
        
        // When
        TensionDTO dto = mapper.toTensionDTO(tension);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getDescription()).isEqualTo("Need product role");
        assertThat(dto.getCurrentState()).isEqualTo("No dedicated role");
        assertThat(dto.getDesiredState()).isEqualTo("Have clear role");
        assertThat(dto.getExamples()).containsExactly("Example 1", "Example 2");
        assertThat(dto.getContext()).isEqualTo("Additional context");
    }
    
    @Test
    void shouldMapTensionDTOToEntity() {
        // Given
        TensionDTO dto = TensionDTO.builder()
            .description("Need product role")
            .currentState("No dedicated role")
            .desiredState("Have clear role")
            .examples(List.of("Example 1"))
            .context("Context")
            .build();
        
        // When
        Tension tension = mapper.toTension(dto);
        
        // Then
        assertThat(tension).isNotNull();
        assertThat(tension.getDescription()).isEqualTo("Need product role");
        assertThat(tension.getCurrentState()).isEqualTo("No dedicated role");
        assertThat(tension.getDesiredState()).isEqualTo("Have clear role");
    }
    
    @Test
    void shouldMapCreateRequestToEntity() {
        // Given
        TensionDTO tensionDTO = TensionDTO.builder()
            .description("Need product role")
            .currentState("No dedicated role")
            .desiredState("Have clear role")
            .build();
        
        CreateProposalRequest request = CreateProposalRequest.builder()
            .title("Create Product Role")
            .tension(tensionDTO)
            .proposalType("ROLE_MODIFICATION")
            .circleId("circle-123")
            .proposerId("partner-456")
            .build();
        
        // When
        Proposal proposal = mapper.toEntity(request);
        
        // Then
        assertThat(proposal).isNotNull();
        assertThat(proposal.getTitle()).isEqualTo("Create Product Role");
        assertThat(proposal.getTension()).isNotNull();
    }
    
    @Test
    void shouldMapDecisionEventToDTO() {
        // Given
        PartnerId actorId = PartnerId.generate();
        DecisionEvent event = DecisionEvent.create(
            DecisionEventType.PROPOSAL_CREATED,
            actorId,
            "Proposal created"
        );
        
        // When
        DecisionEventDTO dto = mapper.toDecisionEventDTO(event);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getEventType()).isEqualTo("PROPOSAL_CREATED");
        assertThat(dto.getActorId()).isEqualTo(actorId.getValue());
        assertThat(dto.getContent()).isEqualTo("Proposal created");
        assertThat(dto.getTimestamp()).isNotNull();
    }
    
    @Test
    void shouldMapQuestionToDTO() {
        // Given
        PartnerId askerId = PartnerId.generate();
        Question question = Question.create(askerId, "What is the purpose?");
        question.setAnswer("To improve the process");
        
        // When
        QuestionDTO dto = mapper.toQuestionDTO(question);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getQuestion()).isEqualTo("What is the purpose?");
        assertThat(dto.getAnswer()).isEqualTo("To improve the process");
        assertThat(dto.getAskerId()).isEqualTo(askerId.getValue());
    }
    
    @Test
    void shouldMapObjectionToDTO() {
        // Given
        PartnerId objectorId = PartnerId.generate();
        ObjectionCriteria criteria = ObjectionCriteria.builder()
            .reducesCapability(true)
            .limitsAccountability(false)
            .problemNotExistWithout(true)
            .causesHarm(false)
            .build();
        
        Objection objection = Objection.create(objectorId, "This will cause issues", criteria);
        objection.setValid(true);
        
        // When
        ObjectionDTO dto = mapper.toObjectionDTO(objection);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getReasoning()).isEqualTo("This will cause issues");
        assertThat(dto.getObjectorId()).isEqualTo(objectorId.getValue());
        assertThat(dto.isValid()).isTrue();
        assertThat(dto.getCriteria()).isNotNull();
        assertThat(dto.getCriteria().isReducesCapability()).isTrue();
        assertThat(dto.getCriteria().isProblemNotExistWithout()).isTrue();
    }
    
    @Test
    void shouldMapVoteToDTO() {
        // Given
        PartnerId voterId = PartnerId.generate();
        Vote vote = Vote.create(voterId, VoteType.APPROVE, "I support this");
        
        // When
        VoteDTO dto = mapper.toVoteDTO(vote);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getVoteType()).isEqualTo("APPROVE");
        assertThat(dto.getVoterId()).isEqualTo(voterId.getValue());
        assertThat(dto.getComment()).isEqualTo("I support this");
    }
}
