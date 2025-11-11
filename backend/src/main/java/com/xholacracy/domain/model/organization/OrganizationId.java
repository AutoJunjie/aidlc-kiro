package com.xholacracy.domain.model.organization;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 组织ID值对象
 * 值对象特征：不可变、通过值相等
 */
@Embeddable
public class OrganizationId implements Serializable {
    
    private String value;
    
    // JPA需要无参构造函数
    protected OrganizationId() {
    }
    
    private OrganizationId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("OrganizationId value cannot be null or empty");
        }
        this.value = value;
    }
    
    /**
     * 生成新的组织ID
     */
    public static OrganizationId generate() {
        return new OrganizationId(UUID.randomUUID().toString());
    }
    
    /**
     * 从已有值创建组织ID
     */
    public static OrganizationId of(String value) {
        return new OrganizationId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationId that = (OrganizationId) o;
        return Objects.equals(value, that.value);
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
