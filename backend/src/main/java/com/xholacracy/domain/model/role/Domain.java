package com.xholacracy.domain.model.role;

import com.xholacracy.domain.exception.ValidationException;
import jakarta.persistence.*;
import java.util.Objects;

/**
 * Domain值对象
 * 表示角色拥有独占控制权的资源、资产或活动范围
 */
@Entity
@Table(name = "domains")
public class Domain {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "control_type")
    private DomainControlType controlType;
    
    // JPA需要无参构造函数
    protected Domain() {
    }
    
    private Domain(String name, String description, DomainControlType controlType) {
        this.name = name;
        this.description = description;
        this.controlType = controlType != null ? controlType : DomainControlType.EXCLUSIVE;
    }
    
    /**
     * 创建Domain
     */
    public static Domain create(String name, String description) {
        return create(name, description, DomainControlType.EXCLUSIVE);
    }
    
    /**
     * 创建Domain with control type
     */
    public static Domain create(String name, String description, DomainControlType controlType) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("name", "Domain name cannot be null or empty");
        }
        return new Domain(name, description, controlType);
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public DomainControlType getControlType() {
        return controlType;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Domain domain = (Domain) o;
        return Objects.equals(name, domain.name) && 
               Objects.equals(description, domain.description) &&
               controlType == domain.controlType;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, description, controlType);
    }
    
    @Override
    public String toString() {
        return "Domain{" +
                "name='" + name + '\'' +
                ", controlType=" + controlType +
                '}';
    }
}
