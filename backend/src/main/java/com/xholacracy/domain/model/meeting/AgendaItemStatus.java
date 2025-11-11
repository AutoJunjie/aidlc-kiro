package com.xholacracy.domain.model.meeting;

/**
 * 议程项状态枚举
 * 
 * 表示会议议程项的处理状态
 */
public enum AgendaItemStatus {
    /**
     * 待处理 - 议程项尚未开始处理
     */
    PENDING,
    
    /**
     * 进行中 - 议程项正在处理
     */
    IN_PROGRESS,
    
    /**
     * 已完成 - 议程项已处理完成
     */
    COMPLETED,
    
    /**
     * 已跳过 - 议程项被跳过
     */
    SKIPPED
}
