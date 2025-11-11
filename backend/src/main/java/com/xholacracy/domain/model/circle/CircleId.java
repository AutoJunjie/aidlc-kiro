package com.xholacracy.domain.model.circle;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 圈子ID值对象
 * 值对象特征：不可变、通过值相等
 */
@Embeddable
public class CircleId implements Serializable {
    
    private String value;
    
    // JPA需要无参构造函数
    protected CircleId() {
    }
    
    private CircleId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("CircleId value cannot be null or empty");
        }
        this.value = value;
    }
    
    /**
     * 生成新的圈子ID
     */
    public static CircleId generate() {
        return new CircleId(UUID.randomUUID().toString());
    }
    
    /**
     * 从已有值创建圈子ID
     */
    public static CircleId of(String value) {
        return new CircleId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CircleId circleId = (CircleId) o;
        return Objects.equals(value, circleId.value);
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
