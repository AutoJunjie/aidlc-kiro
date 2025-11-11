package com.xholacracy.domain.model.role;

import com.xholacracy.domain.model.circle.CircleId;
import java.util.List;
import java.util.Optional;

/**
 * Role Repository接口
 * 定义角色的持久化操作
 */
public interface RoleRepository {
    
    /**
     * 保存角色
     */
    Role save(Role role);
    
    /**
     * 根据ID查找角色
     */
    Optional<Role> findById(RoleId roleId);
    
    /**
     * 根据圈子ID查找所有角色
     */
    List<Role> findByCircleId(CircleId circleId);
    
    /**
     * 查找所有角色
     */
    List<Role> findAll();
    
    /**
     * 删除角色
     */
    void delete(Role role);
    
    /**
     * 根据ID删除角色
     */
    void deleteById(RoleId roleId);
    
    /**
     * 检查角色是否存在
     */
    boolean existsById(RoleId roleId);
}
