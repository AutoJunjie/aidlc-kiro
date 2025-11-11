package com.xholacracy.domain.model.meeting;

import com.xholacracy.domain.model.circle.CircleId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 会议仓储接口
 * 
 * 定义会议聚合的持久化操作
 */
public interface MeetingRepository {
    
    /**
     * 保存会议
     * 
     * @param meeting 会议
     * @return 保存后的会议
     */
    GovernanceMeeting save(GovernanceMeeting meeting);
    
    /**
     * 根据ID查找会议
     * 
     * @param id 会议ID
     * @return 会议（如果存在）
     */
    Optional<GovernanceMeeting> findById(MeetingId id);
    
    /**
     * 根据圈子ID查找所有会议
     * 
     * @param circleId 圈子ID
     * @return 会议列表
     */
    List<GovernanceMeeting> findByCircleId(CircleId circleId);
    
    /**
     * 根据圈子ID和状态查找会议
     * 
     * @param circleId 圈子ID
     * @param status 会议状态
     * @return 会议列表
     */
    List<GovernanceMeeting> findByCircleIdAndStatus(CircleId circleId, MeetingStatus status);
    
    /**
     * 查找指定日期范围内的会议
     * 
     * @param circleId 圈子ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 会议列表
     */
    List<GovernanceMeeting> findByCircleIdAndDateRange(CircleId circleId, 
                                                        LocalDateTime startDate, 
                                                        LocalDateTime endDate);
    
    /**
     * 查找所有已完成的会议（归档）
     * 
     * @param circleId 圈子ID
     * @return 会议列表
     */
    List<GovernanceMeeting> findCompletedMeetings(CircleId circleId);
    
    /**
     * 删除会议
     * 
     * @param id 会议ID
     */
    void deleteById(MeetingId id);
    
    /**
     * 检查会议是否存在
     * 
     * @param id 会议ID
     * @return 如果存在则返回true
     */
    boolean existsById(MeetingId id);
}
