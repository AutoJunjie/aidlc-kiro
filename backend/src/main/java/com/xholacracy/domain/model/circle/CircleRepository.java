package com.xholacracy.domain.model.circle;

import com.xholacracy.domain.model.organization.OrganizationId;

import java.util.List;
import java.util.Optional;

/**
 * Circle仓储接口
 * 定义圈子聚合根的持久化操作
 * 
 * 实现将在基础设施层提供
 */
public interface CircleRepository {
    
    /**
     * 保存圈子
     * 
     * @param circle 要保存的圈子
     * @return 保存后的圈子
     */
    Circle save(Circle circle);
    
    /**
     * 根据ID查找圈子
     * 
     * @param id 圈子ID
     * @return 圈子（如果存在）
     */
    Optional<Circle> findById(CircleId id);
    
    /**
     * 查找所有圈子
     * 
     * @return 所有圈子列表
     */
    List<Circle> findAll();
    
    /**
     * 根据组织ID查找所有圈子
     * 
     * @param organizationId 组织ID
     * @return 圈子列表
     */
    List<Circle> findByOrganizationId(OrganizationId organizationId);
    
    /**
     * 根据父圈子ID查找子圈子
     * 
     * @param parentCircleId 父圈子ID
     * @return 子圈子列表
     */
    List<Circle> findByParentCircleId(CircleId parentCircleId);
    
    /**
     * 查找组织的Anchor Circle
     * 
     * @param organizationId 组织ID
     * @return Anchor Circle（如果存在）
     */
    Optional<Circle> findAnchorCircleByOrganizationId(OrganizationId organizationId);
    
    /**
     * 检查圈子是否存在
     * 
     * @param id 圈子ID
     * @return 如果存在返回true
     */
    boolean existsById(CircleId id);
    
    /**
     * 删除圈子
     * 
     * @param circle 要删除的圈子
     */
    void delete(Circle circle);
    
    /**
     * 根据ID删除圈子
     * 
     * @param id 圈子ID
     */
    void deleteById(CircleId id);
}
