package com.xholacracy.domain.model.organization;

import com.xholacracy.domain.model.circle.Circle;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.exception.ValidationException;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Organization聚合根
 * 代表整个组织实体，是所有圈子、角色和伙伴的最高层级容器
 * 
 * 业务规则：
 * - 每个组织必须有唯一的名称
 * - 创建组织时自动创建Anchor Circle
 * - 每个组织有且仅有一个Anchor Circle
 */
@Entity
@Table(name = "organizations")
@EntityListeners(AuditingEntityListener.class)
public class Organization {
    
    @EmbeddedId
    private OrganizationId id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "anchor_circle_id")
    private Circle anchorCircle;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // JPA需要无参构造函数
    protected Organization() {
    }
    
    private Organization(OrganizationId id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    /**
     * 工厂方法：创建新组织
     * 自动创建Anchor Circle
     * 
     * @param name 组织名称
     * @param description 组织描述
     * @return 新创建的组织
     * @throws ValidationException 如果名称为空
     */
    public static Organization create(String name, String description) {
        validateName(name);
        
        OrganizationId orgId = OrganizationId.generate();
        Organization organization = new Organization(orgId, name, description);
        
        // 自动创建Anchor Circle
        organization.anchorCircle = Circle.createAnchorCircle(orgId);
        
        return organization;
    }
    
    /**
     * 更新组织信息
     * 
     * @param name 新的组织名称
     * @param description 新的组织描述
     * @throws ValidationException 如果名称为空
     */
    public void updateInfo(String name, String description) {
        validateName(name);
        this.name = name;
        this.description = description;
    }
    
    /**
     * 验证组织名称
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("name", "Organization name cannot be null or empty");
        }
        if (name.length() > 255) {
            throw new ValidationException("name", "Organization name cannot exceed 255 characters");
        }
    }
    
    // Getters
    
    public OrganizationId getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Circle getAnchorCircle() {
        return anchorCircle;
    }
    
    public CircleId getAnchorCircleId() {
        return anchorCircle != null ? anchorCircle.getId() : null;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
