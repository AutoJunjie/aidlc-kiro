package com.xholacracy.domain.service;

import com.xholacracy.domain.exception.DomainException;
import com.xholacracy.domain.exception.ResourceNotFoundException;
import com.xholacracy.domain.model.circle.Circle;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.circle.CircleRepository;
import com.xholacracy.domain.model.circle.SpecialRoleType;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.role.Role;
import com.xholacracy.domain.model.role.RoleAssignment;
import com.xholacracy.domain.model.role.RoleId;
import com.xholacracy.domain.model.role.RoleRepository;

import java.util.List;
import java.util.Optional;

/**
 * 角色分配领域服务
 * 负责角色分配的业务规则和自动分配逻辑
 */
public class RoleAssignmentService {
    
    private final RoleRepository roleRepository;
    private final CircleRepository circleRepository;
    
    public RoleAssignmentService(
            RoleRepository roleRepository,
            CircleRepository circleRepository) {
        this.roleRepository = roleRepository;
        this.circleRepository = circleRepository;
    }
    
    /**
     * 分配角色给伙伴
     * 如果角色没有分配给任何人，会自动分配给 Circle Lead
     * 
     * @param roleId 角色ID
     * @param partnerId 伙伴ID
     * @param assignedBy 分配人ID
     * @return 角色分配记录
     * @throws ResourceNotFoundException 如果角色不存在
     * @throws DomainException 如果分配冲突
     */
    public RoleAssignment assignRole(RoleId roleId, PartnerId partnerId, PartnerId assignedBy) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role", roleId.getValue()));
        
        // 检查是否存在分配冲突
        checkAssignmentConflict(role, partnerId);
        
        // 执行分配
        RoleAssignment assignment = role.assignToPartner(partnerId, assignedBy);
        
        roleRepository.save(role);
        
        return assignment;
    }
    
    /**
     * 自动分配未分配的角色给 Circle Lead
     * 根据 Holacracy 规则，如果角色没有分配给任何人，自动分配给 Circle Lead
     * 
     * @param roleId 角色ID
     * @throws ResourceNotFoundException 如果角色或圈子不存在
     */
    public void autoAssignToCircleLead(RoleId roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role", roleId.getValue()));
        
        // 如果角色已经有分配，不需要自动分配
        if (!role.getAssignments().isEmpty()) {
            return;
        }
        
        // 获取角色所在的圈子
        Circle circle = circleRepository.findById(role.getCircleId())
            .orElseThrow(() -> new ResourceNotFoundException("Circle", role.getCircleId().getValue()));
        
        // 找到 Circle Lead 角色
        Optional<Role> circleLeadRole = circle.getRoles().stream()
            .filter(r -> r.isSpecialRole() && r.getSpecialRoleType() == SpecialRoleType.CIRCLE_LEAD)
            .findFirst();
        
        if (circleLeadRole.isEmpty()) {
            throw new DomainException(
                "CIRCLE_LEAD_NOT_FOUND",
                "Circle Lead role not found in circle: " + circle.getId().getValue()
            );
        }
        
        // 获取 Circle Lead 的分配
        List<RoleAssignment> circleLeadAssignments = circleLeadRole.get().getAssignments();
        if (circleLeadAssignments.isEmpty()) {
            throw new DomainException(
                "CIRCLE_LEAD_NOT_ASSIGNED",
                "Circle Lead role is not assigned to any partner"
            );
        }
        
        // 自动分配给 Circle Lead
        PartnerId circleLeadPartnerId = circleLeadAssignments.get(0).getPartnerId();
        role.assignToPartner(circleLeadPartnerId, circleLeadPartnerId);
        
        roleRepository.save(role);
    }
    
    /**
     * 检查角色分配冲突
     * 
     * @param role 角色
     * @param partnerId 伙伴ID
     * @throws DomainException 如果存在冲突
     */
    private void checkAssignmentConflict(Role role, PartnerId partnerId) {
        // 检查是否已经分配给该伙伴
        boolean alreadyAssigned = role.getAssignments().stream()
            .anyMatch(assignment -> assignment.getPartnerId().equals(partnerId));
        
        if (alreadyAssigned) {
            throw new DomainException(
                "ROLE_ALREADY_ASSIGNED",
                String.format("Role %s is already assigned to partner %s",
                    role.getId().getValue(),
                    partnerId.getValue())
            );
        }
        
        // 特殊角色只能分配给一个人
        if (role.isSpecialRole() && !role.getAssignments().isEmpty()) {
            throw new DomainException(
                "SPECIAL_ROLE_SINGLE_ASSIGNMENT",
                String.format("Special role %s can only be assigned to one partner",
                    role.getName())
            );
        }
    }
    
    /**
     * 移除角色分配
     * 
     * @param roleId 角色ID
     * @param partnerId 伙伴ID
     * @throws ResourceNotFoundException 如果角色不存在
     * @throws DomainException 如果分配不存在
     */
    public void removeAssignment(RoleId roleId, PartnerId partnerId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role", roleId.getValue()));
        
        // 查找分配记录
        Optional<RoleAssignment> assignment = role.getAssignments().stream()
            .filter(a -> a.getPartnerId().equals(partnerId))
            .findFirst();
        
        if (assignment.isEmpty()) {
            throw new DomainException(
                "ASSIGNMENT_NOT_FOUND",
                String.format("Role %s is not assigned to partner %s",
                    role.getId().getValue(),
                    partnerId.getValue())
            );
        }
        
        // 移除分配
        role.removeAssignment(assignment.get());
        
        roleRepository.save(role);
        
        // 如果角色没有分配了，自动分配给 Circle Lead
        if (role.getAssignments().isEmpty() && !role.isSpecialRole()) {
            autoAssignToCircleLead(roleId);
        }
    }
    
    /**
     * 检查伙伴是否可以被分配到角色
     * 
     * @param roleId 角色ID
     * @param partnerId 伙伴ID
     * @return 是否可以分配
     */
    public boolean canAssignRole(RoleId roleId, PartnerId partnerId) {
        try {
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId.getValue()));
            
            checkAssignmentConflict(role, partnerId);
            return true;
        } catch (DomainException e) {
            return false;
        }
    }
    
    /**
     * 获取角色的所有分配
     * 
     * @param roleId 角色ID
     * @return 分配列表
     * @throws ResourceNotFoundException 如果角色不存在
     */
    public List<RoleAssignment> getRoleAssignments(RoleId roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role", roleId.getValue()));
        
        return role.getAssignments();
    }
}
