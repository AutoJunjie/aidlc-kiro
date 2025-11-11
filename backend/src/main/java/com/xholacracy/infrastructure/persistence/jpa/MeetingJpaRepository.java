package com.xholacracy.infrastructure.persistence.jpa;

import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.meeting.GovernanceMeeting;
import com.xholacracy.domain.model.meeting.MeetingId;
import com.xholacracy.domain.model.meeting.MeetingStatus;
import com.xholacracy.domain.model.partner.PartnerId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * GovernanceMeeting的Spring Data JPA Repository
 * 提供基础的CRUD操作和自定义查询
 */
@Repository
public interface MeetingJpaRepository extends JpaRepository<GovernanceMeeting, MeetingId> {
    
    /**
     * 根据圈子ID查找会议
     * 
     * @param circleId 圈子ID
     * @return 会议列表
     */
    List<GovernanceMeeting> findByCircleId(CircleId circleId);
    
    /**
     * 根据圈子ID分页查找会议
     * 
     * @param circleId 圈子ID
     * @param pageable 分页参数
     * @return 会议分页结果
     */
    Page<GovernanceMeeting> findByCircleId(CircleId circleId, Pageable pageable);
    
    /**
     * 根据状态查找会议
     * 
     * @param status 会议状态
     * @return 会议列表
     */
    List<GovernanceMeeting> findByStatus(MeetingStatus status);
    
    /**
     * 根据状态分页查找会议
     * 
     * @param status 会议状态
     * @param pageable 分页参数
     * @return 会议分页结果
     */
    Page<GovernanceMeeting> findByStatus(MeetingStatus status, Pageable pageable);
    
    /**
     * 根据圈子ID和状态查找会议
     * 
     * @param circleId 圈子ID
     * @param status 会议状态
     * @return 会议列表
     */
    List<GovernanceMeeting> findByCircleIdAndStatus(CircleId circleId, MeetingStatus status);
    
    /**
     * 根据圈子ID和状态分页查找会议
     * 
     * @param circleId 圈子ID
     * @param status 会议状态
     * @param pageable 分页参数
     * @return 会议分页结果
     */
    Page<GovernanceMeeting> findByCircleIdAndStatus(CircleId circleId, MeetingStatus status, Pageable pageable);
    
    /**
     * 根据协调员ID查找会议
     * 
     * @param facilitatorId 协调员ID
     * @return 会议列表
     */
    List<GovernanceMeeting> findByFacilitatorId(PartnerId facilitatorId);
    
    /**
     * 根据秘书ID查找会议
     * 
     * @param secretaryId 秘书ID
     * @return 会议列表
     */
    List<GovernanceMeeting> findBySecretaryId(PartnerId secretaryId);
    
    /**
     * 根据计划日期范围查找会议
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 会议列表
     */
    @Query("SELECT m FROM GovernanceMeeting m " +
           "WHERE m.scheduledDate >= :startDate " +
           "AND m.scheduledDate <= :endDate " +
           "ORDER BY m.scheduledDate ASC")
    List<GovernanceMeeting> findByScheduledDateBetween(@Param("startDate") LocalDateTime startDate, 
                                                        @Param("endDate") LocalDateTime endDate);
    
    /**
     * 根据圈子ID和计划日期范围查找会议
     * 
     * @param circleId 圈子ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 会议列表
     */
    @Query("SELECT m FROM GovernanceMeeting m " +
           "WHERE m.circleId = :circleId " +
           "AND m.scheduledDate >= :startDate " +
           "AND m.scheduledDate <= :endDate " +
           "ORDER BY m.scheduledDate ASC")
    List<GovernanceMeeting> findByCircleIdAndScheduledDateBetween(
            @Param("circleId") CircleId circleId,
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * 查找会议及其议程（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 会议ID
     * @return 会议（如果存在）
     */
    @Query("SELECT m FROM GovernanceMeeting m " +
           "LEFT JOIN FETCH m.agenda " +
           "WHERE m.id = :id")
    Optional<GovernanceMeeting> findByIdWithAgenda(@Param("id") MeetingId id);
    
    /**
     * 查找会议及其所有关联数据（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 会议ID
     * @return 会议（如果存在）
     */
    @Query("SELECT DISTINCT m FROM GovernanceMeeting m " +
           "LEFT JOIN FETCH m.agenda " +
           "LEFT JOIN FETCH m.participants " +
           "LEFT JOIN FETCH m.meetingRecord " +
           "WHERE m.id = :id")
    Optional<GovernanceMeeting> findByIdWithAllRelations(@Param("id") MeetingId id);
    
    /**
     * 查找圈子最近的会议
     * 
     * @param circleId 圈子ID
     * @param pageable 分页参数
     * @return 会议列表
     */
    @Query("SELECT m FROM GovernanceMeeting m " +
           "WHERE m.circleId = :circleId " +
           "ORDER BY m.scheduledDate DESC")
    List<GovernanceMeeting> findRecentMeetingsByCircleId(@Param("circleId") CircleId circleId, Pageable pageable);
    
    /**
     * 统计圈子的会议数量
     * 
     * @param circleId 圈子ID
     * @return 会议数量
     */
    long countByCircleId(CircleId circleId);
    
    /**
     * 统计圈子特定状态的会议数量
     * 
     * @param circleId 圈子ID
     * @param status 会议状态
     * @return 会议数量
     */
    long countByCircleIdAndStatus(CircleId circleId, MeetingStatus status);
}
