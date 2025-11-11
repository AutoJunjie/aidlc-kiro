package com.xholacracy.domain.model.role;

import com.xholacracy.domain.exception.ValidationException;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.circle.SpecialRoleType;
import com.xholacracy.domain.model.partner.PartnerId;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Role实体
 * 组织中的一个功能单元，定义了特定的目的、职责和领域权限
 */
@Entity
@Table(name = "roles")
public class Role {
    
    @EmbeddedId
    private RoleId id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String purpose;
    
    @ElementCollection
    @CollectionTable(name = "role_accountabilities", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "accountability")
    private List<String> accountabilities = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "role_id")
    private List<Domain> domains = new ArrayList<>();
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "circle_id"))
    private CircleId circleId;
    
    @Column(name = "is_special_role")
    private boolean isSpecialRole;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "special_role_type")
    private SpecialRoleType specialRoleType;
    
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleAssignment> assignments = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // JPA需要无参构造函数
    protected Role() {
    }
    
    private Role(String name, String purpose, CircleId circleId, boolean isSpecialRole, SpecialRoleType specialRoleType) {
        this.id = RoleId.generate();
        this.name = name;
        this.purpose = purpose;
        this.circleId = circleId;
        this.isSpecialRole = isSpecialRole;
        this.specialRoleType = specialRoleType;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 创建普通角色
     */
    public static Role create(String name, String purpose, CircleId circleId) {
        validateRoleCreation(name, purpose, circleId);
        return new Role(name, purpose, circleId, false, null);
    }
    
    /**
     * 创建特殊角色
     */
    public static Role createSpecialRole(String name, CircleId circleId, SpecialRoleType type) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("name", "Role name cannot be null or empty");
        }
        if (circleId == null) {
            throw new ValidationException("circleId", "CircleId cannot be null");
        }
        if (type == null) {
            throw new ValidationException("specialRoleType", "SpecialRoleType cannot be null for special roles");
        }
        
        return new Role(name, type.getDescription(), circleId, true, type);
    }
    
    /**
     * 分配角色给伙伴
     */
    public RoleAssignment assignToPartner(PartnerId partnerId, PartnerId assignedBy) {
        if (partnerId == null) {
            throw new ValidationException("partnerId", "PartnerId cannot be null");
        }
        if (assignedBy == null) {
            throw new ValidationException("assignedBy", "AssignedBy cannot be null");
        }
        
        RoleAssignment assignment = RoleAssignment.create(this, partnerId, assignedBy);
        this.assignments.add(assignment);
        this.updatedAt = LocalDateTime.now();
        return assignment;
    }
    
    /**
     * 添加领域
     */
    public void addDomain(Domain domain) {
        if (domain == null) {
            throw new ValidationException("domain", "Domain cannot be null");
        }
        this.domains.add(domain);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 添加职责
     */
    public void addAccountability(String accountability) {
        if (accountability == null || accountability.trim().isEmpty()) {
            throw new ValidationException("accountability", "Accountability cannot be null or empty");
        }
        this.accountabilities.add(accountability);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 检查角色是否分配给指定伙伴
     */
    public boolean isAssignedTo(PartnerId partnerId) {
        if (partnerId == null) {
            return false;
        }
        return assignments.stream()
                .anyMatch(a -> a.getPartnerId().equals(partnerId));
    }
    
    /**
     * 更新角色信息
     */
    public void updateInfo(String name, String purpose) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (purpose != null && !purpose.trim().isEmpty()) {
            this.purpose = purpose;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 移除角色分配
     */
    public void removeAssignment(RoleAssignment assignment) {
        if (assignment != null) {
            this.assignments.remove(assignment);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    private static void validateRoleCreation(String name, String purpose, CircleId circleId) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("name", "Role name cannot be null or empty");
        }
        if (purpose == null || purpose.trim().isEmpty()) {
            throw new ValidationException("purpose", "Role purpose cannot be null or empty");
        }
        if (circleId == null) {
            throw new ValidationException("circleId", "CircleId cannot be null");
        }
    }
    
    // Getters
    
    public RoleId getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPurpose() {
        return purpose;
    }
    
    public List<String> getAccountabilities() {
        return Collections.unmodifiableList(accountabilities);
    }
    
    public List<Domain> getDomains() {
        return Collections.unmodifiableList(domains);
    }
    
    public CircleId getCircleId() {
        return circleId;
    }
    
    public boolean isSpecialRole() {
        return isSpecialRole;
    }
    
    public SpecialRoleType getSpecialRoleType() {
        return specialRoleType;
    }
    
    public List<RoleAssignment> getAssignments() {
        return Collections.unmodifiableList(assignments);
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
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isSpecialRole=" + isSpecialRole +
                ", specialRoleType=" + specialRoleType +
                '}';
    }
}
