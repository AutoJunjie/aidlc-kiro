package com.xholacracy.domain.model.meeting;

/**
 * 治理会议状态枚举
 * 
 * 表示会议的生命周期状态
 */
public enum MeetingStatus {
    /**
     * 已安排 - 会议已创建但尚未开始
     */
    SCHEDULED,
    
    /**
     * 进行中 - 会议正在进行
     */
    IN_PROGRESS,
    
    /**
     * 已完成 - 会议已正常结束
     */
    COMPLETED,
    
    /**
     * 已取消 - 会议被取消
     */
    CANCELLED
}
