package com.xholacracy.domain.model.circle;

import com.xholacracy.domain.model.organization.OrganizationId;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.role.Role;
import com.xholacracy.domain.exception.DomainException;
import com.xholacracy.domain.exception.ValidationException;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Circle聚合根
 * 代表一组相关角色的集合，形成组织结构的层级单元
 * 
 * 业务规则：
 * - 每个圈子必须有名称和目的
 * - 创建圈子时自动创建四个特殊角色（Circle Lead, Facilitator, Secretary, Circle Rep）
 * - 圈子可以包含子圈子，形成层级结构
 * - 圈子可以包含多个角色
 * - 特殊角色不能被删除
 */
@Entity
@Table(name = "circles")
@EntityListeners(AuditingEntityListener.class)
public class Circle {
    
    @EmbeddedId
    private CircleId id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String purpose;
    
    @ElementCollection
    @CollectionTable(name = "circle_accountabilities", 
                    joinColumns = @JoinColumn(name = "circle_id"))
    @Column(name = "accountability")
    private List<String> accountabilities = new ArrayList<>();
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "parent_circle_id"))
    private CircleId parentCircleId;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "organization_id"))
    private OrganizationId organizationId;
    
    @OneToMany(mappedBy = "parentCircle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Circle> subCircles = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_circle_id", insertable = false, updatable = false)
    private Circle parentCircle;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "circle_id")
    private List<Role> roles = new ArrayList<>();
    
    @Embedded
    private SpecialRoles specialRoles;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // JPA需要无参构造函数
    protected Circle() {
    }
    
    private Circle(CircleId id, String name, String purpose, 
                  CircleId parentCircleId, OrganizationId organizationId) {
        this.id = id;
        this.name = name;
        this.purpose = purpose;
        this.parentCircleId = parentCircleId;
        this.organizationId = organizationId;
        this.specialRoles = SpecialRoles.create();
    }
    
    /**
     * 工厂方法：创建Anchor Circle
     * Anchor Circle是组织的根圈子
     * 
     * @param orgId 组织ID
     * @return 新创建的Anchor Circle
     */
    public static Circle createAnchorCircle(OrganizationId orgId) {
        CircleId circleId = CircleId.generate();
        Circle circle = new Circle(
            circleId,
            "Anchor Circle",
            "The broadest circle of the organization",
            null, // Anchor Circle没有父圈子
            orgId
        );
        
        // 自动创建四个特殊角色
        circle.initializeSpecialRoles();
        
        return circle;
    }
    
    /**
     * 工厂方法：创建子圈子
     * 
     * @param name 圈子名称
     * @param purpose 圈子目的
     * @param parentId 父圈子ID
     * @param orgId 组织ID
     * @return 新创建的子圈子
     * @throws ValidationException 如果名称或目的为空
     */
    public static Circle createSubCircle(String name, String purpose, 
                                        CircleId parentId, OrganizationId orgId) {
        validateName(name);
        validatePurpose(purpose);
        
        if (parentId == null) {
            throw new ValidationException("parentCircleId", "Parent circle ID cannot be null for sub-circle");
        }
        
        CircleId circleId = CircleId.generate();
        Circle circle = new Circle(circleId, name, purpose, parentId, orgId);
        
        // 自动创建四个特殊角色
        circle.initializeSpecialRoles();
        
        return circle;
    }
    
    /**
     * 初始化四个特殊角色
     */
    private void initializeSpecialRoles() {
        // 创建Circle Lead角色
        Role circleLead = Role.createSpecialRole(
            SpecialRoleType.CIRCLE_LEAD.getDisplayName(),
            this.id,
            SpecialRoleType.CIRCLE_LEAD
        );
        this.roles.add(circleLead);
        this.specialRoles.setCircleLeadRoleId(circleLead.getId());
        
        // 创建Facilitator角色
        Role facilitator = Role.createSpecialRole(
            SpecialRoleType.FACILITATOR.getDisplayName(),
            this.id,
            SpecialRoleType.FACILITATOR
        );
        this.roles.add(facilitator);
        this.specialRoles.setFacilitatorRoleId(facilitator.getId());
        
        // 创建Secretary角色
        Role secretary = Role.createSpecialRole(
            SpecialRoleType.SECRETARY.getDisplayName(),
            this.id,
            SpecialRoleType.SECRETARY
        );
        this.roles.add(secretary);
        this.specialRoles.setSecretaryRoleId(secretary.getId());
        
        // 创建Circle Rep角色
        Role circleRep = Role.createSpecialRole(
            SpecialRoleType.CIRCLE_REP.getDisplayName(),
            this.id,
            SpecialRoleType.CIRCLE_REP
        );
        this.roles.add(circleRep);
        this.specialRoles.setCircleRepRoleId(circleRep.getId());
    }
    
    /**
     * 添加子圈子
     * 
     * @param subCircle 子圈子
     * @throws DomainException 如果子圈子的父ID不匹配
     */
    public void addSubCircle(Circle subCircle) {
        if (subCircle == null) {
            throw new IllegalArgumentException("Sub-circle cannot be null");
        }
        
        if (!this.id.equals(subCircle.getParentCircleId())) {
            throw DomainException.of("Sub-circle parent ID must match this circle's ID");
        }
        
        this.subCircles.add(subCircle);
    }
    
    /**
     * 添加角色
     * 
     * @param role 角色
     * @throws DomainException 如果角色的圈子ID不匹配
     */
    public void addRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        
        if (!this.id.equals(role.getCircleId())) {
            throw DomainException.of("Role circle ID must match this circle's ID");
        }
        
        this.roles.add(role);
    }
    
    /**
     * 分配特殊角色给伙伴
     * 
     * @param roleType 特殊角色类型
     * @param partnerId 伙伴ID
     */
    public void assignSpecialRole(SpecialRoleType roleType, PartnerId partnerId) {
        // 这个方法的完整实现将在任务3中完成
        // 现在只是占位符
    }
    
    /**
     * 更新圈子信息
     * 
     * @param name 新名称
     * @param purpose 新目的
     * @param accountabilities 新职责列表
     */
    public void updateInfo(String name, String purpose, List<String> accountabilities) {
        validateName(name);
        validatePurpose(purpose);
        
        this.name = name;
        this.purpose = purpose;
        
        if (accountabilities != null) {
            this.accountabilities.clear();
            this.accountabilities.addAll(accountabilities);
        }
    }
    
    /**
     * 添加职责
     */
    public void addAccountability(String accountability) {
        if (accountability == null || accountability.trim().isEmpty()) {
            throw new ValidationException("accountability", "Accountability cannot be null or empty");
        }
        this.accountabilities.add(accountability);
    }
    
    /**
     * 验证圈子名称
     */
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("name", "Circle name cannot be null or empty");
        }
        if (name.length() > 255) {
            throw new ValidationException("name", "Circle name cannot exceed 255 characters");
        }
    }
    
    /**
     * 验证圈子目的
     */
    private static void validatePurpose(String purpose) {
        if (purpose == null || purpose.trim().isEmpty()) {
            throw new ValidationException("purpose", "Circle purpose cannot be null or empty");
        }
        if (purpose.length() > 1000) {
            throw new ValidationException("purpose", "Circle purpose cannot exceed 1000 characters");
        }
    }
    
    // Getters
    
    public CircleId getId() {
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
    
    public CircleId getParentCircleId() {
        return parentCircleId;
    }
    
    public OrganizationId getOrganizationId() {
        return organizationId;
    }
    
    public List<Circle> getSubCircles() {
        return Collections.unmodifiableList(subCircles);
    }
    
    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles);
    }
    
    public SpecialRoles getSpecialRoles() {
        return specialRoles;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * 检查是否是Anchor Circle
     */
    public boolean isAnchorCircle() {
        return parentCircleId == null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle circle = (Circle) o;
        return Objects.equals(id, circle.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Circle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isAnchorCircle=" + isAnchorCircle() +
                '}';
    }
}
