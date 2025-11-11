package com.xholacracy.infrastructure.persistence.jpa;

import com.xholacracy.domain.model.circle.Circle;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.organization.OrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Circle的Spring Data JPA Repository
 * 提供基础的CRUD操作和自定义查询
 */
@Repository
public interface CircleJpaRepository extends JpaRepository<Circle, CircleId> {
    
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
     * 查找组织的Anchor Circle（父圈子ID为null）
     * 
     * @param organizationId 组织ID
     * @return Anchor Circle（如果存在）
     */
    @Query("SELECT c FROM Circle c " +
           "WHERE c.organizationId = :organizationId " +
           "AND c.parentCircleId IS NULL")
    Optional<Circle> findAnchorCircleByOrganizationId(@Param("organizationId") OrganizationId organizationId);
    
    /**
     * 查找圈子及其角色（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 圈子ID
     * @return 圈子（如果存在）
     */
    @Query("SELECT c FROM Circle c " +
           "LEFT JOIN FETCH c.roles " +
           "WHERE c.id = :id")
    Optional<Circle> findByIdWithRoles(@Param("id") CircleId id);
    
    /**
     * 查找圈子及其角色和分配（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 圈子ID
     * @return 圈子（如果存在）
     */
    @Query("SELECT DISTINCT c FROM Circle c " +
           "LEFT JOIN FETCH c.roles r " +
           "LEFT JOIN FETCH r.assignments " +
           "WHERE c.id = :id")
    Optional<Circle> findByIdWithRolesAndAssignments(@Param("id") CircleId id);
    
    /**
     * 查找圈子及其子圈子（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 圈子ID
     * @return 圈子（如果存在）
     */
    @Query("SELECT c FROM Circle c " +
           "LEFT JOIN FETCH c.subCircles " +
           "WHERE c.id = :id")
    Optional<Circle> findByIdWithSubCircles(@Param("id") CircleId id);
    
    /**
     * 根据名称查找圈子
     * 
     * @param name 圈子名称
     * @return 圈子列表
     */
    List<Circle> findByName(String name);
    
    /**
     * 根据名称和组织ID查找圈子
     * 
     * @param name 圈子名称
     * @param organizationId 组织ID
     * @return 圈子（如果存在）
     */
    Optional<Circle> findByNameAndOrganizationId(String name, OrganizationId organizationId);
}
