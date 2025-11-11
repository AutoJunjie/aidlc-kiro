package com.xholacracy.domain.model.organization;

import java.util.List;
import java.util.Optional;

/**
 * Organization仓储接口
 * 定义组织聚合根的持久化操作
 * 
 * 实现将在基础设施层提供
 */
public interface OrganizationRepository {
    
    /**
     * 保存组织
     * 
     * @param organization 要保存的组织
     * @return 保存后的组织
     */
    Organization save(Organization organization);
    
    /**
     * 根据ID查找组织
     * 
     * @param id 组织ID
     * @return 组织（如果存在）
     */
    Optional<Organization> findById(OrganizationId id);
    
    /**
     * 查找所有组织
     * 
     * @return 所有组织列表
     */
    List<Organization> findAll();
    
    /**
     * 根据名称查找组织
     * 
     * @param name 组织名称
     * @return 组织（如果存在）
     */
    Optional<Organization> findByName(String name);
    
    /**
     * 检查组织是否存在
     * 
     * @param id 组织ID
     * @return 如果存在返回true
     */
    boolean existsById(OrganizationId id);
    
    /**
     * 删除组织
     * 
     * @param organization 要删除的组织
     */
    void delete(Organization organization);
    
    /**
     * 根据ID删除组织
     * 
     * @param id 组织ID
     */
    void deleteById(OrganizationId id);
}
