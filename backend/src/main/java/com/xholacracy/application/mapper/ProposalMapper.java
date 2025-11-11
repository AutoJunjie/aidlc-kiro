package com.xholacracy.application.mapper;

import com.xholacracy.application.dto.proposal.*;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * MapStruct mapper for Proposal entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface ProposalMapper {
    
    @Mapping(target = "id", expression = "java(proposal.getId().getValue())")
    @Mapping(target = "circleId", expression = "java(proposal.getCircleId().getValue())")
    @Mapping(target = "proposerId", expression = "java(proposal.getProposerId().getValue())")
    @Mapping(target = "proposerName", ignore = true)
    @Mapping(target = "proposalType", expression = "java(proposal.getProposalType().name())")
    @Mapping(target = "status", expression = "java(proposal.getStatus().name())")
    ProposalDTO toDTO(Proposal proposal);
    
    // Note: Use Proposal.create() factory method instead of mapping from request
    // Entities should be created through domain factory methods to ensure business rules
    
    void updateFromRequest(UpdateProposalRequest request, @MappingTarget Proposal proposal);
    
    TensionDTO toTensionDTO(Tension tension);
    
    Tension toTension(TensionDTO dto);
    
    @Mapping(target = "eventType", expression = "java(event.getEventType().name())")
    @Mapping(target = "actorId", expression = "java(event.getActorId() != null ? event.getActorId().getValue() : null)")
    @Mapping(target = "actorName", ignore = true)
    DecisionEventDTO toDecisionEventDTO(DecisionEvent event);
    
    List<DecisionEventDTO> toDecisionEventDTOList(List<DecisionEvent> events);
    
    @Mapping(target = "id", expression = "java(question.getId().getValue())")
    @Mapping(target = "askerId", expression = "java(question.getAskerId().getValue())")
    @Mapping(target = "askerName", ignore = true)
    QuestionDTO toQuestionDTO(Question question);
    
    List<QuestionDTO> toQuestionDTOList(List<Question> questions);
    
    @Mapping(target = "id", expression = "java(reaction.getId().getValue())")
    @Mapping(target = "reactorId", expression = "java(reaction.getReactorId().getValue())")
    @Mapping(target = "reactorName", ignore = true)
    ReactionDTO toReactionDTO(Reaction reaction);
    
    List<ReactionDTO> toReactionDTOList(List<Reaction> reactions);
    
    @Mapping(target = "id", expression = "java(amendment.getId().getValue())")
    AmendmentDTO toAmendmentDTO(Amendment amendment);
    
    List<AmendmentDTO> toAmendmentDTOList(List<Amendment> amendments);
    
    @Mapping(target = "id", expression = "java(objection.getId().getValue())")
    @Mapping(target = "objectorId", expression = "java(objection.getObjectorId().getValue())")
    @Mapping(target = "objectorName", ignore = true)
    @Mapping(target = "validatedBy", expression = "java(objection.getValidatedBy() != null ? objection.getValidatedBy().getValue() : null)")
    ObjectionDTO toObjectionDTO(Objection objection);
    
    List<ObjectionDTO> toObjectionDTOList(List<Objection> objections);
    
    ObjectionCriteriaDTO toObjectionCriteriaDTO(ObjectionCriteria criteria);
    
    @Mapping(target = "id", expression = "java(vote.getId().getValue())")
    @Mapping(target = "voteType", expression = "java(vote.getVoteType().name())")
    @Mapping(target = "voterId", expression = "java(vote.getVoterId().getValue())")
    @Mapping(target = "voterName", ignore = true)
    VoteDTO toVoteDTO(Vote vote);
    
    List<VoteDTO> toVoteDTOList(List<Vote> votes);
    
    default ProposalId mapId(String id) {
        return id != null ? ProposalId.of(id) : null;
    }
    
    default String mapId(ProposalId id) {
        return id != null ? id.getValue() : null;
    }
    
    default CircleId mapCircleId(String id) {
        return id != null ? CircleId.of(id) : null;
    }
    
    default PartnerId mapPartnerId(String id) {
        return id != null ? PartnerId.of(id) : null;
    }
    
    default ProposalType mapProposalType(String type) {
        return type != null ? ProposalType.valueOf(type) : null;
    }
}
