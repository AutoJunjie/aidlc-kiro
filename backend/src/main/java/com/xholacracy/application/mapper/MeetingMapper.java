package com.xholacracy.application.mapper;

import com.xholacracy.application.dto.meeting.*;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.meeting.*;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.ProposalId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * MapStruct mapper for GovernanceMeeting entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface MeetingMapper {
    
    @Mapping(target = "id", expression = "java(meeting.getId().getValue())")
    @Mapping(target = "circleId", expression = "java(meeting.getCircleId().getValue())")
    @Mapping(target = "circleName", ignore = true)
    @Mapping(target = "durationMinutes", expression = "java(meeting.getDuration() != null ? (int)meeting.getDuration().toMinutes() : null)")
    @Mapping(target = "status", expression = "java(meeting.getStatus().name())")
    @Mapping(target = "facilitatorId", expression = "java(meeting.getFacilitatorId() != null ? meeting.getFacilitatorId().getValue() : null)")
    @Mapping(target = "facilitatorName", ignore = true)
    @Mapping(target = "secretaryId", expression = "java(meeting.getSecretaryId() != null ? meeting.getSecretaryId().getValue() : null)")
    @Mapping(target = "secretaryName", ignore = true)
    @Mapping(target = "participantIds", expression = "java(mapPartnerIds(meeting.getParticipants()))")
    @Mapping(target = "participantNames", ignore = true)
    MeetingDTO toDTO(GovernanceMeeting meeting);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "circleId", ignore = true)
    @Mapping(target = "scheduledDate", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "facilitatorId", ignore = true)
    @Mapping(target = "secretaryId", ignore = true)
    @Mapping(target = "agenda", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "meetingRecord", ignore = true)
    GovernanceMeeting toEntity(CreateMeetingRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "circleId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "facilitatorId", ignore = true)
    @Mapping(target = "secretaryId", ignore = true)
    @Mapping(target = "agenda", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "meetingRecord", ignore = true)
    void updateEntity(UpdateMeetingRequest request, @MappingTarget GovernanceMeeting meeting);
    
    MeetingAgendaDTO toAgendaDTO(MeetingAgenda agenda);
    
    @Mapping(target = "id", expression = "java(item.getId().getValue())")
    @Mapping(target = "proposalId", expression = "java(item.getProposalId().getValue())")
    @Mapping(target = "proposalTitle", ignore = true)
    @Mapping(target = "status", expression = "java(item.getStatus().name())")
    AgendaItemDTO toAgendaItemDTO(AgendaItem item);
    
    List<AgendaItemDTO> toAgendaItemDTOList(List<AgendaItem> items);
    
    MeetingRecordDTO toRecordDTO(MeetingRecord record);
    
    @Mapping(target = "proposalId", expression = "java(outcome.getProposalId().getValue())")
    @Mapping(target = "proposalTitle", ignore = true)
    @Mapping(target = "outcome", expression = "java(outcome.getOutcome().name())")
    ProposalOutcomeDTO toProposalOutcomeDTO(ProposalOutcome outcome);
    
    List<ProposalOutcomeDTO> toProposalOutcomeDTOList(List<ProposalOutcome> outcomes);
    
    default MeetingId mapId(String id) {
        return id != null ? MeetingId.of(id) : null;
    }
    
    default String mapId(MeetingId id) {
        return id != null ? id.getValue() : null;
    }
    
    default CircleId mapCircleId(String id) {
        return id != null ? CircleId.of(id) : null;
    }
    
    default PartnerId mapPartnerId(String id) {
        return id != null ? PartnerId.of(id) : null;
    }
    
    default ProposalId mapProposalId(String id) {
        return id != null ? ProposalId.of(id) : null;
    }
    
    default List<String> mapPartnerIds(List<PartnerId> partnerIds) {
        if (partnerIds == null) return null;
        return partnerIds.stream()
            .map(PartnerId::getValue)
            .toList();
    }
    
    default List<PartnerId> mapPartnerIdList(List<String> ids) {
        if (ids == null) return null;
        return ids.stream()
            .map(PartnerId::of)
            .toList();
    }
}
