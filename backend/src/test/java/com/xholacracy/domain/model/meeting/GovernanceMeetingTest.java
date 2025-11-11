package com.xholacracy.domain.model.meeting;

import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.ProposalId;
import com.xholacracy.domain.model.proposal.ProposalStatus;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 治理会议聚合根测试
 */
class GovernanceMeetingTest {
    
    @Test
    void shouldCreateGovernanceMeeting() {
        // Given
        CircleId circleId = CircleId.generate();
        LocalDateTime scheduledDate = LocalDateTime.now().plusDays(1);
        Duration duration = Duration.ofHours(2);
        
        // When
        GovernanceMeeting meeting = GovernanceMeeting.create(circleId, scheduledDate, duration);
        
        // Then
        assertThat(meeting.getId()).isNotNull();
        assertThat(meeting.getCircleId()).isEqualTo(circleId);
        assertThat(meeting.getScheduledDate()).isEqualTo(scheduledDate);
        assertThat(meeting.getDuration()).isEqualTo(duration);
        assertThat(meeting.getStatus()).isEqualTo(MeetingStatus.SCHEDULED);
        assertThat(meeting.getAgenda()).isNotNull();
        assertThat(meeting.getAgenda().isEmpty()).isTrue();
        assertThat(meeting.getMeetingRecord()).isNotNull();
        assertThat(meeting.getParticipants()).isEmpty();
    }
    
    @Test
    void shouldSetFacilitatorAndSecretary() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        PartnerId facilitatorId = PartnerId.generate();
        PartnerId secretaryId = PartnerId.generate();
        
        // When
        meeting.setFacilitator(facilitatorId);
        meeting.setSecretary(secretaryId);
        
        // Then
        assertThat(meeting.getFacilitatorId()).isEqualTo(facilitatorId);
        assertThat(meeting.getSecretaryId()).isEqualTo(secretaryId);
    }
    
    @Test
    void shouldAddAgendaItem() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        ProposalId proposalId = ProposalId.generate();
        
        // When
        meeting.addAgendaItem(proposalId);
        
        // Then
        assertThat(meeting.getAgenda().size()).isEqualTo(1);
        assertThat(meeting.getAgenda().getItems().get(0).getProposalId()).isEqualTo(proposalId);
    }
    
    @Test
    void shouldRemoveAgendaItem() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        ProposalId proposalId1 = ProposalId.generate();
        ProposalId proposalId2 = ProposalId.generate();
        meeting.addAgendaItem(proposalId1);
        meeting.addAgendaItem(proposalId2);
        
        // When
        meeting.removeAgendaItem(proposalId1);
        
        // Then
        assertThat(meeting.getAgenda().size()).isEqualTo(1);
        assertThat(meeting.getAgenda().getItems().get(0).getProposalId()).isEqualTo(proposalId2);
    }
    
    @Test
    void shouldNotRemoveAgendaItemFromNonScheduledMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        ProposalId proposalId = ProposalId.generate();
        meeting.addAgendaItem(proposalId);
        meeting.start();
        
        // When & Then
        assertThatThrownBy(() -> meeting.removeAgendaItem(proposalId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot remove agenda items");
    }
    
    @Test
    void shouldReorderAgenda() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        ProposalId proposalId1 = ProposalId.generate();
        ProposalId proposalId2 = ProposalId.generate();
        ProposalId proposalId3 = ProposalId.generate();
        meeting.addAgendaItem(proposalId1);
        meeting.addAgendaItem(proposalId2);
        meeting.addAgendaItem(proposalId3);
        
        // When
        meeting.reorderAgenda(Arrays.asList(proposalId3, proposalId1, proposalId2));
        
        // Then
        assertThat(meeting.getAgenda().getItems().get(0).getProposalId()).isEqualTo(proposalId3);
        assertThat(meeting.getAgenda().getItems().get(1).getProposalId()).isEqualTo(proposalId1);
        assertThat(meeting.getAgenda().getItems().get(2).getProposalId()).isEqualTo(proposalId2);
    }
    
    @Test
    void shouldAddParticipant() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        PartnerId partnerId = PartnerId.generate();
        
        // When
        meeting.addParticipant(partnerId);
        
        // Then
        assertThat(meeting.getParticipants()).hasSize(1);
        assertThat(meeting.getParticipants()).contains(partnerId);
        assertThat(meeting.isParticipant(partnerId)).isTrue();
    }
    
    @Test
    void shouldNotAddDuplicateParticipant() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        PartnerId partnerId = PartnerId.generate();
        
        // When
        meeting.addParticipant(partnerId);
        meeting.addParticipant(partnerId);
        
        // Then
        assertThat(meeting.getParticipants()).hasSize(1);
    }
    
    @Test
    void shouldAddMultipleParticipants() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        List<PartnerId> partnerIds = Arrays.asList(
                PartnerId.generate(),
                PartnerId.generate(),
                PartnerId.generate()
        );
        
        // When
        meeting.addParticipants(partnerIds);
        
        // Then
        assertThat(meeting.getParticipants()).hasSize(3);
    }
    
    @Test
    void shouldRemoveParticipant() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        PartnerId partnerId = PartnerId.generate();
        meeting.addParticipant(partnerId);
        
        // When
        meeting.removeParticipant(partnerId);
        
        // Then
        assertThat(meeting.getParticipants()).isEmpty();
    }
    
    @Test
    void shouldNotRemoveParticipantFromNonScheduledMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        PartnerId partnerId = PartnerId.generate();
        meeting.addParticipant(partnerId);
        meeting.start();
        
        // When & Then
        assertThatThrownBy(() -> meeting.removeParticipant(partnerId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot remove participants");
    }
    
    @Test
    void shouldStartMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        
        // When
        meeting.start();
        
        // Then
        assertThat(meeting.getStatus()).isEqualTo(MeetingStatus.IN_PROGRESS);
        assertThat(meeting.getActualStartTime()).isNotNull();
    }
    
    @Test
    void shouldNotStartNonScheduledMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        meeting.start();
        
        // When & Then
        assertThatThrownBy(() -> meeting.start())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only scheduled meetings can be started");
    }
    
    @Test
    void shouldEndMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        meeting.start();
        
        // When
        meeting.end();
        
        // Then
        assertThat(meeting.getStatus()).isEqualTo(MeetingStatus.COMPLETED);
        assertThat(meeting.getActualEndTime()).isNotNull();
        assertThat(meeting.getActualDuration()).isNotNull();
    }
    
    @Test
    void shouldNotEndNonInProgressMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        
        // When & Then
        assertThatThrownBy(() -> meeting.end())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only in-progress meetings can be ended");
    }
    
    @Test
    void shouldCancelScheduledMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        
        // When
        meeting.cancel();
        
        // Then
        assertThat(meeting.getStatus()).isEqualTo(MeetingStatus.CANCELLED);
    }
    
    @Test
    void shouldNotCancelCompletedMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        meeting.start();
        meeting.end();
        
        // When & Then
        assertThatThrownBy(() -> meeting.cancel())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot cancel completed meetings");
    }
    
    @Test
    void shouldStartAgendaItem() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        ProposalId proposalId = ProposalId.generate();
        meeting.addAgendaItem(proposalId);
        meeting.start();
        
        // When
        meeting.startAgendaItem(proposalId);
        
        // Then
        assertThat(meeting.getAgenda().getItems().get(0).getStatus())
                .isEqualTo(AgendaItemStatus.IN_PROGRESS);
    }
    
    @Test
    void shouldNotStartAgendaItemInNonInProgressMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        ProposalId proposalId = ProposalId.generate();
        meeting.addAgendaItem(proposalId);
        
        // When & Then
        assertThatThrownBy(() -> meeting.startAgendaItem(proposalId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Can only process agenda items during in-progress meetings");
    }
    
    @Test
    void shouldCompleteAgendaItem() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        ProposalId proposalId = ProposalId.generate();
        meeting.addAgendaItem(proposalId);
        meeting.start();
        meeting.startAgendaItem(proposalId);
        
        // When
        meeting.completeAgendaItem(proposalId, "Proposal approved");
        
        // Then
        assertThat(meeting.getAgenda().getItems().get(0).getStatus())
                .isEqualTo(AgendaItemStatus.COMPLETED);
        assertThat(meeting.getAgenda().getItems().get(0).getNotes())
                .isEqualTo("Proposal approved");
    }
    
    @Test
    void shouldSkipAgendaItem() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        ProposalId proposalId = ProposalId.generate();
        meeting.addAgendaItem(proposalId);
        meeting.start();
        
        // When
        meeting.skipAgendaItem(proposalId, "Proposer not present");
        
        // Then
        assertThat(meeting.getAgenda().getItems().get(0).getStatus())
                .isEqualTo(AgendaItemStatus.SKIPPED);
    }
    
    @Test
    void shouldRecordProposalOutcome() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        ProposalId proposalId = ProposalId.generate();
        meeting.addAgendaItem(proposalId);
        meeting.start();
        ProposalOutcome outcome = ProposalOutcome.approved(proposalId, "Approved");
        
        // When
        meeting.recordProposalOutcome(outcome);
        
        // Then
        assertThat(meeting.getMeetingRecord().getProposalOutcomeCount()).isEqualTo(1);
        assertThat(meeting.getMeetingRecord().getProposalOutcome(proposalId)).isEqualTo(outcome);
    }
    
    @Test
    void shouldRecordAttendance() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        PartnerId partnerId = PartnerId.generate();
        
        // When
        meeting.recordAttendance(partnerId);
        
        // Then
        assertThat(meeting.getMeetingRecord().isAttended(partnerId)).isTrue();
        assertThat(meeting.getMeetingRecord().getAttendeeCount()).isEqualTo(1);
    }
    
    @Test
    void shouldRecordBatchAttendance() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        List<PartnerId> partnerIds = Arrays.asList(
                PartnerId.generate(),
                PartnerId.generate()
        );
        
        // When
        meeting.recordAttendance(partnerIds);
        
        // Then
        assertThat(meeting.getMeetingRecord().getAttendeeCount()).isEqualTo(2);
    }
    
    @Test
    void shouldRecordCheckIn() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        meeting.start();
        String notes = "Everyone shared their focus";
        
        // When
        meeting.recordCheckIn(notes);
        
        // Then
        assertThat(meeting.getMeetingRecord().getCheckInNotes()).isEqualTo(notes);
    }
    
    @Test
    void shouldAddNotes() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        meeting.start();
        
        // When
        meeting.addNotes("Important discussion");
        
        // Then
        assertThat(meeting.getMeetingRecord().getAdditionalNotes()).contains("Important discussion");
    }
    
    @Test
    void shouldRecordClosing() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        meeting.start();
        String notes = "Thank you all";
        
        // When
        meeting.recordClosing(notes);
        
        // Then
        assertThat(meeting.getMeetingRecord().getClosingNotes()).isEqualTo(notes);
    }
    
    @Test
    void shouldRescheduleMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        LocalDateTime newDate = LocalDateTime.now().plusDays(2);
        
        // When
        meeting.reschedule(newDate);
        
        // Then
        assertThat(meeting.getScheduledDate()).isEqualTo(newDate);
    }
    
    @Test
    void shouldNotRescheduleNonScheduledMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        meeting.start();
        
        // When & Then
        assertThatThrownBy(() -> meeting.reschedule(LocalDateTime.now().plusDays(2)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Can only reschedule scheduled meetings");
    }
    
    @Test
    void shouldUpdateDuration() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        Duration newDuration = Duration.ofHours(3);
        
        // When
        meeting.updateDuration(newDuration);
        
        // Then
        assertThat(meeting.getDuration()).isEqualTo(newDuration);
    }
    
    @Test
    void shouldNotUpdateDurationForNonScheduledMeeting() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        meeting.start();
        
        // When & Then
        assertThatThrownBy(() -> meeting.updateDuration(Duration.ofHours(3)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Can only update duration for scheduled meetings");
    }
    
    @Test
    void shouldCheckIfMeetingIsOverdue() {
        // Given
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        GovernanceMeeting meeting = GovernanceMeeting.create(
                CircleId.generate(),
                pastDate,
                Duration.ofHours(1)
        );
        
        // When & Then
        assertThat(meeting.isOverdue()).isTrue();
    }
    
    @Test
    void shouldGenerateMeetingSummary() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        meeting.start();
        meeting.recordCheckIn("Good energy");
        meeting.recordAttendance(PartnerId.generate());
        ProposalId proposalId = ProposalId.generate();
        meeting.addAgendaItem(proposalId);
        meeting.recordProposalOutcome(ProposalOutcome.approved(proposalId, "Approved"));
        meeting.recordClosing("Thank you");
        
        // When
        String summary = meeting.generateSummary();
        
        // Then
        assertThat(summary).contains("会议摘要");
        assertThat(summary).contains("参与者");
        assertThat(summary).contains("Good energy");
        assertThat(summary).contains("Approved");
        assertThat(summary).contains("Thank you");
    }
    
    @Test
    void shouldCalculateActualDuration() {
        // Given
        GovernanceMeeting meeting = createScheduledMeeting();
        meeting.start();
        
        // Simulate some time passing
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        meeting.end();
        
        // When
        Duration actualDuration = meeting.getActualDuration();
        
        // Then
        assertThat(actualDuration).isNotNull();
        assertThat(actualDuration.toMillis()).isGreaterThan(0);
    }
    
    @Test
    void shouldHaveEqualityBasedOnId() {
        // Given
        CircleId circleId = CircleId.generate();
        LocalDateTime scheduledDate = LocalDateTime.now().plusDays(1);
        Duration duration = Duration.ofHours(2);
        
        GovernanceMeeting meeting1 = GovernanceMeeting.create(circleId, scheduledDate, duration);
        GovernanceMeeting meeting2 = GovernanceMeeting.create(circleId, scheduledDate, duration);
        
        // When & Then
        assertThat(meeting1).isNotEqualTo(meeting2);
        assertThat(meeting1).isEqualTo(meeting1);
    }
    
    // Helper methods
    
    private GovernanceMeeting createScheduledMeeting() {
        CircleId circleId = CircleId.generate();
        LocalDateTime scheduledDate = LocalDateTime.now().plusDays(1);
        Duration duration = Duration.ofHours(2);
        return GovernanceMeeting.create(circleId, scheduledDate, duration);
    }
}
