package com.xholacracy.domain.model.meeting;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 会议相关枚举测试
 */
class MeetingEnumsTest {
    
    @Test
    void meetingStatusShouldHaveAllExpectedValues() {
        // Given & When
        MeetingStatus[] statuses = MeetingStatus.values();
        
        // Then
        assertThat(statuses).hasSize(4);
        assertThat(statuses).contains(
                MeetingStatus.SCHEDULED,
                MeetingStatus.IN_PROGRESS,
                MeetingStatus.COMPLETED,
                MeetingStatus.CANCELLED
        );
    }
    
    @Test
    void meetingStatusShouldBeConvertibleFromString() {
        // Given
        String statusName = "IN_PROGRESS";
        
        // When
        MeetingStatus status = MeetingStatus.valueOf(statusName);
        
        // Then
        assertThat(status).isEqualTo(MeetingStatus.IN_PROGRESS);
    }
    
    @Test
    void agendaItemStatusShouldHaveAllExpectedValues() {
        // Given & When
        AgendaItemStatus[] statuses = AgendaItemStatus.values();
        
        // Then
        assertThat(statuses).hasSize(4);
        assertThat(statuses).contains(
                AgendaItemStatus.PENDING,
                AgendaItemStatus.IN_PROGRESS,
                AgendaItemStatus.COMPLETED,
                AgendaItemStatus.SKIPPED
        );
    }
    
    @Test
    void agendaItemStatusShouldBeConvertibleFromString() {
        // Given
        String statusName = "COMPLETED";
        
        // When
        AgendaItemStatus status = AgendaItemStatus.valueOf(statusName);
        
        // Then
        assertThat(status).isEqualTo(AgendaItemStatus.COMPLETED);
    }
}
