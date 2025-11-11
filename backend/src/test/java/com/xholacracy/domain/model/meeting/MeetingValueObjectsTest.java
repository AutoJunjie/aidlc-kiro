package com.xholacracy.domain.model.meeting;

import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.ProposalId;
import com.xholacracy.domain.model.proposal.ProposalStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 会议相关值对象测试
 */
class MeetingValueObjectsTest {
    
    // AgendaItem Tests
    
    @Test
    void shouldCreateAgendaItem() {
        // Given
        ProposalId proposalId = ProposalId.generate();
        int orderIndex = 0;
        
        // When
        AgendaItem item = AgendaItem.create(proposalId, orderIndex);
        
        // Then
        assertThat(item.getProposalId()).isEqualTo(proposalId);
        assertThat(item.getOrderIndex()).isEqualTo(orderIndex);
        assertThat(item.getStatus()).isEqualTo(AgendaItemStatus.PENDING);
        assertThat(item.getStartedAt()).isNull();
        assertThat(item.getCompletedAt()).isNull();
    }
    
    @Test
    void shouldStartAgendaItem() {
        // Given
        AgendaItem item = AgendaItem.create(ProposalId.generate(), 0);
        
        // When
        item.start();
        
        // Then
        assertThat(item.getStatus()).isEqualTo(AgendaItemStatus.IN_PROGRESS);
        assertThat(item.getStartedAt()).isNotNull();
    }
    
    @Test
    void shouldNotStartNonPendingAgendaItem() {
        // Given
        AgendaItem item = AgendaItem.create(ProposalId.generate(), 0);
        item.start();
        
        // When & Then
        assertThatThrownBy(() -> item.start())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only pending agenda items can be started");
    }
    
    @Test
    void shouldCompleteAgendaItem() {
        // Given
        AgendaItem item = AgendaItem.create(ProposalId.generate(), 0);
        item.start();
        String notes = "Proposal approved";
        
        // When
        item.complete(notes);
        
        // Then
        assertThat(item.getStatus()).isEqualTo(AgendaItemStatus.COMPLETED);
        assertThat(item.getCompletedAt()).isNotNull();
        assertThat(item.getNotes()).isEqualTo(notes);
    }
    
    @Test
    void shouldNotCompleteNonInProgressAgendaItem() {
        // Given
        AgendaItem item = AgendaItem.create(ProposalId.generate(), 0);
        
        // When & Then
        assertThatThrownBy(() -> item.complete("notes"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only in-progress agenda items can be completed");
    }
    
    @Test
    void shouldSkipAgendaItem() {
        // Given
        AgendaItem item = AgendaItem.create(ProposalId.generate(), 0);
        String reason = "Proposer not present";
        
        // When
        item.skip(reason);
        
        // Then
        assertThat(item.getStatus()).isEqualTo(AgendaItemStatus.SKIPPED);
        assertThat(item.getNotes()).isEqualTo(reason);
    }
    
    @Test
    void shouldNotSkipCompletedAgendaItem() {
        // Given
        AgendaItem item = AgendaItem.create(ProposalId.generate(), 0);
        item.start();
        item.complete("Done");
        
        // When & Then
        assertThatThrownBy(() -> item.skip("reason"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot skip completed agenda items");
    }
    
    @Test
    void shouldUpdateOrderIndex() {
        // Given
        AgendaItem item = AgendaItem.create(ProposalId.generate(), 0);
        
        // When
        item.updateOrderIndex(5);
        
        // Then
        assertThat(item.getOrderIndex()).isEqualTo(5);
    }
    
    // MeetingAgenda Tests
    
    @Test
    void shouldCreateEmptyAgenda() {
        // When
        MeetingAgenda agenda = MeetingAgenda.create();
        
        // Then
        assertThat(agenda.isEmpty()).isTrue();
        assertThat(agenda.size()).isEqualTo(0);
        assertThat(agenda.getItems()).isEmpty();
    }
    
    @Test
    void shouldAddItemToAgenda() {
        // Given
        MeetingAgenda agenda = MeetingAgenda.create();
        ProposalId proposalId = ProposalId.generate();
        
        // When
        agenda.addItem(proposalId);
        
        // Then
        assertThat(agenda.size()).isEqualTo(1);
        assertThat(agenda.isEmpty()).isFalse();
        assertThat(agenda.getItems().get(0).getProposalId()).isEqualTo(proposalId);
        assertThat(agenda.getItems().get(0).getOrderIndex()).isEqualTo(0);
    }
    
    @Test
    void shouldNotAddDuplicateProposal() {
        // Given
        MeetingAgenda agenda = MeetingAgenda.create();
        ProposalId proposalId = ProposalId.generate();
        agenda.addItem(proposalId);
        
        // When & Then
        assertThatThrownBy(() -> agenda.addItem(proposalId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Proposal already in agenda");
    }
    
    @Test
    void shouldRemoveItemFromAgenda() {
        // Given
        MeetingAgenda agenda = MeetingAgenda.create();
        ProposalId proposalId1 = ProposalId.generate();
        ProposalId proposalId2 = ProposalId.generate();
        agenda.addItem(proposalId1);
        agenda.addItem(proposalId2);
        
        // When
        agenda.removeItem(proposalId1);
        
        // Then
        assertThat(agenda.size()).isEqualTo(1);
        assertThat(agenda.getItems().get(0).getProposalId()).isEqualTo(proposalId2);
        assertThat(agenda.getItems().get(0).getOrderIndex()).isEqualTo(0); // Reordered
    }
    
    @Test
    void shouldReorderAgendaItems() {
        // Given
        MeetingAgenda agenda = MeetingAgenda.create();
        ProposalId proposalId1 = ProposalId.generate();
        ProposalId proposalId2 = ProposalId.generate();
        ProposalId proposalId3 = ProposalId.generate();
        agenda.addItem(proposalId1);
        agenda.addItem(proposalId2);
        agenda.addItem(proposalId3);
        
        // When
        agenda.reorder(Arrays.asList(proposalId3, proposalId1, proposalId2));
        
        // Then
        assertThat(agenda.getItems().get(0).getProposalId()).isEqualTo(proposalId3);
        assertThat(agenda.getItems().get(0).getOrderIndex()).isEqualTo(0);
        assertThat(agenda.getItems().get(1).getProposalId()).isEqualTo(proposalId1);
        assertThat(agenda.getItems().get(1).getOrderIndex()).isEqualTo(1);
        assertThat(agenda.getItems().get(2).getProposalId()).isEqualTo(proposalId2);
        assertThat(agenda.getItems().get(2).getOrderIndex()).isEqualTo(2);
    }
    
    @Test
    void shouldStartAgendaItemByProposalId() {
        // Given
        MeetingAgenda agenda = MeetingAgenda.create();
        ProposalId proposalId = ProposalId.generate();
        agenda.addItem(proposalId);
        
        // When
        agenda.startItem(proposalId);
        
        // Then
        assertThat(agenda.getItems().get(0).getStatus()).isEqualTo(AgendaItemStatus.IN_PROGRESS);
    }
    
    @Test
    void shouldCompleteAgendaItemByProposalId() {
        // Given
        MeetingAgenda agenda = MeetingAgenda.create();
        ProposalId proposalId = ProposalId.generate();
        agenda.addItem(proposalId);
        agenda.startItem(proposalId);
        
        // When
        agenda.completeItem(proposalId, "Approved");
        
        // Then
        assertThat(agenda.getItems().get(0).getStatus()).isEqualTo(AgendaItemStatus.COMPLETED);
        assertThat(agenda.getItems().get(0).getNotes()).isEqualTo("Approved");
    }
    
    @Test
    void shouldGetCurrentItem() {
        // Given
        MeetingAgenda agenda = MeetingAgenda.create();
        ProposalId proposalId1 = ProposalId.generate();
        ProposalId proposalId2 = ProposalId.generate();
        agenda.addItem(proposalId1);
        agenda.addItem(proposalId2);
        agenda.startItem(proposalId2);
        
        // When
        AgendaItem currentItem = agenda.getCurrentItem().orElse(null);
        
        // Then
        assertThat(currentItem).isNotNull();
        assertThat(currentItem.getProposalId()).isEqualTo(proposalId2);
    }
    
    @Test
    void shouldGetNextPendingItem() {
        // Given
        MeetingAgenda agenda = MeetingAgenda.create();
        ProposalId proposalId1 = ProposalId.generate();
        ProposalId proposalId2 = ProposalId.generate();
        agenda.addItem(proposalId1);
        agenda.addItem(proposalId2);
        
        // When
        AgendaItem nextItem = agenda.getNextPendingItem().orElse(null);
        
        // Then
        assertThat(nextItem).isNotNull();
        assertThat(nextItem.getProposalId()).isEqualTo(proposalId1);
    }
    
    @Test
    void shouldCheckIfAllCompleted() {
        // Given
        MeetingAgenda agenda = MeetingAgenda.create();
        ProposalId proposalId1 = ProposalId.generate();
        ProposalId proposalId2 = ProposalId.generate();
        agenda.addItem(proposalId1);
        agenda.addItem(proposalId2);
        
        // When - not all completed
        assertThat(agenda.isAllCompleted()).isFalse();
        
        // When - all completed
        agenda.startItem(proposalId1);
        agenda.completeItem(proposalId1, "Done");
        agenda.skipItem(proposalId2, "Skipped");
        
        // Then
        assertThat(agenda.isAllCompleted()).isTrue();
    }
    
    // ProposalOutcome Tests
    
    @Test
    void shouldCreateProposalOutcome() {
        // Given
        ProposalId proposalId = ProposalId.generate();
        ProposalStatus status = ProposalStatus.APPROVED;
        String outcome = "Approved";
        String notes = "No objections";
        
        // When
        ProposalOutcome proposalOutcome = ProposalOutcome.create(proposalId, status, outcome, notes);
        
        // Then
        assertThat(proposalOutcome.getProposalId()).isEqualTo(proposalId);
        assertThat(proposalOutcome.getFinalStatus()).isEqualTo(status);
        assertThat(proposalOutcome.getOutcome()).isEqualTo(outcome);
        assertThat(proposalOutcome.getNotes()).isEqualTo(notes);
        assertThat(proposalOutcome.getProcessedAt()).isNotNull();
    }
    
    @Test
    void shouldCreateApprovedOutcome() {
        // Given
        ProposalId proposalId = ProposalId.generate();
        String notes = "Consensus reached";
        
        // When
        ProposalOutcome outcome = ProposalOutcome.approved(proposalId, notes);
        
        // Then
        assertThat(outcome.getFinalStatus()).isEqualTo(ProposalStatus.APPROVED);
        assertThat(outcome.getOutcome()).isEqualTo("Approved");
        assertThat(outcome.getNotes()).isEqualTo(notes);
    }
    
    @Test
    void shouldCreateWithdrawnOutcome() {
        // Given
        ProposalId proposalId = ProposalId.generate();
        String reason = "Proposer withdrew";
        
        // When
        ProposalOutcome outcome = ProposalOutcome.withdrawn(proposalId, reason);
        
        // Then
        assertThat(outcome.getFinalStatus()).isEqualTo(ProposalStatus.WITHDRAWN);
        assertThat(outcome.getOutcome()).isEqualTo("Withdrawn");
        assertThat(outcome.getNotes()).isEqualTo(reason);
    }
    
    @Test
    void shouldCreateDeferredOutcome() {
        // Given
        ProposalId proposalId = ProposalId.generate();
        String reason = "Need more information";
        
        // When
        ProposalOutcome outcome = ProposalOutcome.deferred(proposalId, reason);
        
        // Then
        assertThat(outcome.getFinalStatus()).isEqualTo(ProposalStatus.SUBMITTED);
        assertThat(outcome.getOutcome()).isEqualTo("Deferred");
        assertThat(outcome.getNotes()).isEqualTo(reason);
    }
    
    // MeetingRecord Tests
    
    @Test
    void shouldCreateEmptyMeetingRecord() {
        // When
        MeetingRecord record = MeetingRecord.create();
        
        // Then
        assertThat(record.getCheckInNotes()).isEmpty();
        assertThat(record.getAdditionalNotes()).isEmpty();
        assertThat(record.getClosingNotes()).isEmpty();
        assertThat(record.getAttendees()).isEmpty();
        assertThat(record.getProposalOutcomes()).isEmpty();
        assertThat(record.getAttendeeCount()).isEqualTo(0);
        assertThat(record.getProposalOutcomeCount()).isEqualTo(0);
    }
    
    @Test
    void shouldRecordCheckIn() {
        // Given
        MeetingRecord record = MeetingRecord.create();
        String notes = "Everyone shared their current focus";
        
        // When
        record.recordCheckIn(notes);
        
        // Then
        assertThat(record.getCheckInNotes()).isEqualTo(notes);
    }
    
    @Test
    void shouldAddAdditionalNotes() {
        // Given
        MeetingRecord record = MeetingRecord.create();
        
        // When
        record.addAdditionalNotes("First note");
        record.addAdditionalNotes("Second note");
        
        // Then
        assertThat(record.getAdditionalNotes()).contains("First note");
        assertThat(record.getAdditionalNotes()).contains("Second note");
    }
    
    @Test
    void shouldRecordClosing() {
        // Given
        MeetingRecord record = MeetingRecord.create();
        String notes = "Meeting was productive";
        
        // When
        record.recordClosing(notes);
        
        // Then
        assertThat(record.getClosingNotes()).isEqualTo(notes);
    }
    
    @Test
    void shouldRecordAttendance() {
        // Given
        MeetingRecord record = MeetingRecord.create();
        PartnerId partnerId1 = PartnerId.generate();
        PartnerId partnerId2 = PartnerId.generate();
        
        // When
        record.recordAttendance(partnerId1);
        record.recordAttendance(partnerId2);
        
        // Then
        assertThat(record.getAttendeeCount()).isEqualTo(2);
        assertThat(record.isAttended(partnerId1)).isTrue();
        assertThat(record.isAttended(partnerId2)).isTrue();
    }
    
    @Test
    void shouldNotDuplicateAttendance() {
        // Given
        MeetingRecord record = MeetingRecord.create();
        PartnerId partnerId = PartnerId.generate();
        
        // When
        record.recordAttendance(partnerId);
        record.recordAttendance(partnerId);
        
        // Then
        assertThat(record.getAttendeeCount()).isEqualTo(1);
    }
    
    @Test
    void shouldRecordBatchAttendance() {
        // Given
        MeetingRecord record = MeetingRecord.create();
        List<PartnerId> partnerIds = Arrays.asList(
                PartnerId.generate(),
                PartnerId.generate(),
                PartnerId.generate()
        );
        
        // When
        record.recordAttendance(partnerIds);
        
        // Then
        assertThat(record.getAttendeeCount()).isEqualTo(3);
    }
    
    @Test
    void shouldRecordProposalOutcome() {
        // Given
        MeetingRecord record = MeetingRecord.create();
        ProposalId proposalId = ProposalId.generate();
        ProposalOutcome outcome = ProposalOutcome.approved(proposalId, "Approved");
        
        // When
        record.recordProposalOutcome(outcome);
        
        // Then
        assertThat(record.getProposalOutcomeCount()).isEqualTo(1);
        assertThat(record.getProposalOutcome(proposalId)).isEqualTo(outcome);
    }
    
    @Test
    void shouldReplaceExistingProposalOutcome() {
        // Given
        MeetingRecord record = MeetingRecord.create();
        ProposalId proposalId = ProposalId.generate();
        ProposalOutcome outcome1 = ProposalOutcome.deferred(proposalId, "Deferred");
        ProposalOutcome outcome2 = ProposalOutcome.approved(proposalId, "Approved");
        
        // When
        record.recordProposalOutcome(outcome1);
        record.recordProposalOutcome(outcome2);
        
        // Then
        assertThat(record.getProposalOutcomeCount()).isEqualTo(1);
        assertThat(record.getProposalOutcome(proposalId)).isEqualTo(outcome2);
    }
    
    @Test
    void shouldGenerateMeetingSummary() {
        // Given
        MeetingRecord record = MeetingRecord.create();
        record.recordCheckIn("Good energy");
        record.recordAttendance(PartnerId.generate());
        record.recordAttendance(PartnerId.generate());
        record.recordProposalOutcome(ProposalOutcome.approved(ProposalId.generate(), "Approved"));
        record.addAdditionalNotes("Important discussion");
        record.recordClosing("Thank you all");
        
        // When
        String summary = record.generateSummary();
        
        // Then
        assertThat(summary).contains("会议摘要");
        assertThat(summary).contains("参与者: 2 人");
        assertThat(summary).contains("Good energy");
        assertThat(summary).contains("Approved");
        assertThat(summary).contains("Important discussion");
        assertThat(summary).contains("Thank you all");
    }
}
