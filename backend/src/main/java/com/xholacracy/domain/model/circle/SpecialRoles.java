package com.xholacracy.domain.model.circle;

import com.xholacracy.domain.model.role.RoleId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.util.Objects;

/**
 * SpecialRoles值对象
 * 封装圈子的四个特殊角色ID
 * 
 * 值对象特征：不可变、通过值相等
 */
@Embeddable
public class SpecialRoles {
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "circle_lead_role_id"))
    private RoleId circleLeadRoleId;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "facilitator_role_id"))
    private RoleId facilitatorRoleId;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "secretary_role_id"))
    private RoleId secretaryRoleId;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "circle_rep_role_id"))
    private RoleId circleRepRoleId;
    
    // JPA需要无参构造函数
    protected SpecialRoles() {
    }
    
    private SpecialRoles(RoleId circleLeadRoleId, RoleId facilitatorRoleId, 
                        RoleId secretaryRoleId, RoleId circleRepRoleId) {
        this.circleLeadRoleId = circleLeadRoleId;
        this.facilitatorRoleId = facilitatorRoleId;
        this.secretaryRoleId = secretaryRoleId;
        this.circleRepRoleId = circleRepRoleId;
    }
    
    /**
     * 创建空的特殊角色（角色ID将在创建角色时设置）
     */
    public static SpecialRoles create() {
        return new SpecialRoles(null, null, null, null);
    }
    
    /**
     * 创建包含所有角色ID的特殊角色
     */
    public static SpecialRoles of(RoleId circleLeadRoleId, RoleId facilitatorRoleId,
                                  RoleId secretaryRoleId, RoleId circleRepRoleId) {
        return new SpecialRoles(circleLeadRoleId, facilitatorRoleId, 
                               secretaryRoleId, circleRepRoleId);
    }
    
    // Getters
    
    public RoleId getCircleLeadRoleId() {
        return circleLeadRoleId;
    }
    
    public RoleId getFacilitatorRoleId() {
        return facilitatorRoleId;
    }
    
    public RoleId getSecretaryRoleId() {
        return secretaryRoleId;
    }
    
    public RoleId getCircleRepRoleId() {
        return circleRepRoleId;
    }
    
    /**
     * 设置Circle Lead角色ID
     */
    public void setCircleLeadRoleId(RoleId roleId) {
        this.circleLeadRoleId = roleId;
    }
    
    /**
     * 设置Facilitator角色ID
     */
    public void setFacilitatorRoleId(RoleId roleId) {
        this.facilitatorRoleId = roleId;
    }
    
    /**
     * 设置Secretary角色ID
     */
    public void setSecretaryRoleId(RoleId roleId) {
        this.secretaryRoleId = roleId;
    }
    
    /**
     * 设置Circle Rep角色ID
     */
    public void setCircleRepRoleId(RoleId roleId) {
        this.circleRepRoleId = roleId;
    }
    
    /**
     * 根据特殊角色类型获取角色ID
     */
    public RoleId getRoleId(SpecialRoleType type) {
        return switch (type) {
            case CIRCLE_LEAD -> circleLeadRoleId;
            case FACILITATOR -> facilitatorRoleId;
            case SECRETARY -> secretaryRoleId;
            case CIRCLE_REP -> circleRepRoleId;
        };
    }
    
    /**
     * 根据特殊角色类型设置角色ID
     */
    public void setRoleId(SpecialRoleType type, RoleId roleId) {
        switch (type) {
            case CIRCLE_LEAD -> this.circleLeadRoleId = roleId;
            case FACILITATOR -> this.facilitatorRoleId = roleId;
            case SECRETARY -> this.secretaryRoleId = roleId;
            case CIRCLE_REP -> this.circleRepRoleId = roleId;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialRoles that = (SpecialRoles) o;
        return Objects.equals(circleLeadRoleId, that.circleLeadRoleId) &&
                Objects.equals(facilitatorRoleId, that.facilitatorRoleId) &&
                Objects.equals(secretaryRoleId, that.secretaryRoleId) &&
                Objects.equals(circleRepRoleId, that.circleRepRoleId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(circleLeadRoleId, facilitatorRoleId, secretaryRoleId, circleRepRoleId);
    }
}
