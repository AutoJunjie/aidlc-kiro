package com.xholacracy.domain.model.partner;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 伙伴ID值对象
 * 值对象特征：不可变、通过值相等
 */
@Embeddable
public class PartnerId implements Serializable {
    
    private String value;
    
    // JPA需要无参构造函数
    protected PartnerId() {
    }
    
    private PartnerId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("PartnerId value cannot be null or empty");
        }
        this.value = value;
    }
    
    /**
     * 生成新的伙伴ID
     */
    public static PartnerId generate() {
        return new PartnerId(UUID.randomUUID().toString());
    }
    
    /**
     * 从已有值创建伙伴ID
     */
    public static PartnerId of(String value) {
        return new PartnerId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartnerId partnerId = (PartnerId) o;
        return Objects.equals(value, partnerId.value);
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
