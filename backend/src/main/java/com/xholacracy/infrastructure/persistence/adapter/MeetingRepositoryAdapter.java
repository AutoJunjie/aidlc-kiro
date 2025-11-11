package com.xholacracy.infrastructure.persistence.adapter;

import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.meeting.GovernanceMeeting;
import com.xholacracy.domain.model.meeting.MeetingId;
import com.xholacracy.domain.model.meeting.MeetingRepository;
import com.xholacracy.domain.model.meeting.MeetingStatus;
import com.xholacracy.infrastructure.persistence.jpa.MeetingJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Meeting Repository适配器
 * 将Spring Data JPA Repository适配到领域Repository接口
 */
@Component
@Transactional
public class MeetingRepositoryAdapter implements MeetingRepository {
    
    private final MeetingJpaRepository jpaRepository;
    
    public MeetingRepositoryAdapter(MeetingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public GovernanceMeeting save(GovernanceMeeting meeting) {
        return jpaRepository.save(meeting);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<GovernanceMeeting> findById(MeetingId meetingId) {
        return jpaRepository.findById(meetingId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GovernanceMeeting> findByCircleId(CircleId circleId) {
        return jpaRepository.findByCircleId(circleId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GovernanceMeeting> findByCircleIdAndStatus(CircleId circleId, MeetingStatus status) {
        return jpaRepository.findByCircleIdAndStatus(circleId, status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GovernanceMeeting> findByCircleIdAndDateRange(
            CircleId circleId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByCircleIdAndScheduledDateBetween(circleId, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GovernanceMeeting> findCompletedMeetings(CircleId circleId) {
        return jpaRepository.findByCircleIdAndStatus(circleId, MeetingStatus.COMPLETED);
    }
    
    @Override
    public void deleteById(MeetingId meetingId) {
        jpaRepository.deleteById(meetingId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(MeetingId meetingId) {
        return jpaRepository.existsById(meetingId);
    }
}
