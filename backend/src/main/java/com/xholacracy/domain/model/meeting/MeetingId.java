package com.xholacracy.domain.model.meeting;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 会议ID值对象
 * 值对象特征：不可变、通过值相等
 */
@Embeddable
public class MeetingId implements Serializable {
    
    private String value;
    
    // JPA需要无参构造函数
    protected MeetingId() {
    }
    
    private MeetingId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("MeetingId value cannot be null or empty");
        }
        this.value = value;
    }
    
    /**
     * 生成新的会议ID
     */
    public static MeetingId generate() {
        return new MeetingId(UUID.randomUUID().toString());
    }
    
    /**
     * 从已有值创建会议ID
     */
    public static MeetingId of(String value) {
        return new MeetingId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingId meetingId = (MeetingId) o;
        return Objects.equals(value, meetingId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
