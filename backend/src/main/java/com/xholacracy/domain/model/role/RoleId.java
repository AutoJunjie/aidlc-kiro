package com.xholacracy.domain.model.role;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 角色ID值对象
 * 值对象特征：不可变、通过值相等
 */
@Embeddable
public class RoleId implements Serializable {
    
    private String value;
    
    // JPA需要无参构造函数
    protected RoleId() {
    }
    
    private RoleId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("RoleId value cannot be null or empty");
        }
        this.value = value;
    }
    
    /**
     * 生成新的角色ID
     */
    public static RoleId generate() {
        return new RoleId(UUID.randomUUID().toString());
    }
    
    /**
     * 从已有值创建角色ID
     */
    public static RoleId of(String value) {
        return new RoleId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleId roleId = (RoleId) o;
        return Objects.equals(value, roleId.value);
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
