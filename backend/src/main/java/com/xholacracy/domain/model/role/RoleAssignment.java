package com.xholacracy.domain.model.role;

import com.xholacracy.domain.exception.ValidationException;
import com.xholacracy.domain.model.partner.PartnerId;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * RoleAssignment实体
 * 表示将角色分配给特定伙伴的行为
 */
@Entity
@Table(name = "role_assignments")
public class RoleAssignment {
    
    @Id
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "partner_id", nullable = false))
    private PartnerId partnerId;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "assigned_by"))
    private PartnerId assignedBy;
    
    @Column(name = "assigned_date", nullable = false, updatable = false)
    private LocalDateTime assignedDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // JPA需要无参构造函数
    protected RoleAssignment() {
    }
    
    private RoleAssignment(Role role, PartnerId partnerId, PartnerId assignedBy) {
        this.id = UUID.randomUUID().toString();
        this.role = role;
        this.partnerId = partnerId;
        this.assignedBy = assignedBy;
        this.assignedDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }
    
    /**
     * 创建角色分配
     */
    public static RoleAssignment create(Role role, PartnerId partnerId, PartnerId assignedBy) {
        validateAssignment(role, partnerId, assignedBy);
        return new RoleAssignment(role, partnerId, assignedBy);
    }
    
    private static void validateAssignment(Role role, PartnerId partnerId, PartnerId assignedBy) {
        if (role == null) {
            throw new ValidationException("role", "Role cannot be null");
        }
        if (partnerId == null) {
            throw new ValidationException("partnerId", "PartnerId cannot be null");
        }
        if (assignedBy == null) {
            throw new ValidationException("assignedBy", "AssignedBy cannot be null");
        }
    }
    
    // Getters
    
    public String getId() {
        return id;
    }
    
    public Role getRole() {
        return role;
    }
    
    public PartnerId getPartnerId() {
        return partnerId;
    }
    
    public PartnerId getAssignedBy() {
        return assignedBy;
    }
    
    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleAssignment that = (RoleAssignment) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "RoleAssignment{" +
                "id='" + id + '\'' +
                ", partnerId=" + partnerId +
                ", assignedDate=" + assignedDate +
                '}';
    }
}
