package com.xholacracy.domain.model.partner;

import com.xholacracy.domain.exception.ValidationException;
import com.xholacracy.domain.model.role.Role;
import com.xholacracy.domain.model.role.RoleAssignment;
import com.xholacracy.domain.model.role.RoleId;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Partner实体
 * 填充角色的组织成员，一个伙伴可以同时填充多个角色
 */
@Entity
@Table(name = "partners")
public class Partner {
    
    @EmbeddedId
    private PartnerId id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @OneToMany(mappedBy = "partnerId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleAssignment> roleAssignments = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // JPA需要无参构造函数
    protected Partner() {
    }
    
    private Partner(String name, String email) {
        this.id = PartnerId.generate();
        this.name = name;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 创建伙伴
     */
    public static Partner create(String name, String email) {
        validatePartnerCreation(name, email);
        return new Partner(name, email);
    }
    
    /**
     * 获取伙伴的所有角色
     */
    public List<Role> getRoles() {
        return roleAssignments.stream()
                .map(RoleAssignment::getRole)
                .collect(Collectors.toList());
    }
    
    /**
     * 检查伙伴是否拥有指定角色
     */
    public boolean hasRole(RoleId roleId) {
        if (roleId == null) {
            return false;
        }
        return roleAssignments.stream()
                .anyMatch(assignment -> assignment.getRole().getId().equals(roleId));
    }
    
    /**
     * 更新伙伴信息
     */
    public void updateInfo(String name, String email) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (email != null && !email.trim().isEmpty()) {
            validateEmail(email);
            this.email = email;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    private static void validatePartnerCreation(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("name", "Partner name cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("email", "Partner email cannot be null or empty");
        }
        validateEmail(email);
    }
    
    private static void validateEmail(String email) {
        // 简单的邮箱格式验证
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ValidationException("email", "Invalid email format");
        }
    }
    
    // Getters
    
    public PartnerId getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public List<RoleAssignment> getRoleAssignments() {
        return Collections.unmodifiableList(roleAssignments);
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
        Partner partner = (Partner) o;
        return Objects.equals(id, partner.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Partner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
