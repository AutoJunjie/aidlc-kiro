package com.xholacracy.domain.model.proposal;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 提案ID值对象
 * 值对象特征：不可变、通过值相等
 */
@Embeddable
public class ProposalId implements Serializable {
    
    private String value;
    
    // JPA需要无参构造函数
    protected ProposalId() {
    }
    
    private ProposalId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("ProposalId value cannot be null or empty");
        }
        this.value = value;
    }
    
    /**
     * 生成新的提案ID
     */
    public static ProposalId generate() {
        return new ProposalId(UUID.randomUUID().toString());
    }
    
    /**
     * 从已有值创建提案ID
     */
    public static ProposalId of(String value) {
        return new ProposalId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalId that = (ProposalId) o;
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
