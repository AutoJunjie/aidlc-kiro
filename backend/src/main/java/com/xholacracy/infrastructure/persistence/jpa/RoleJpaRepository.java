package com.xholacracy.infrastructure.persistence.jpa;

import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.circle.SpecialRoleType;
import com.xholacracy.domain.model.role.Role;
import com.xholacracy.domain.model.role.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Role的Spring Data JPA Repository
 * 提供基础的CRUD操作和自定义查询
 */
@Repository
public interface RoleJpaRepository extends JpaRepository<Role, RoleId> {
    
    /**
     * 根据圈子ID查找所有角色
     * 
     * @param circleId 圈子ID
     * @return 角色列表
     */
    List<Role> findByCircleId(CircleId circleId);
    
    /**
     * 根据圈子ID和特殊角色类型查找角色
     * 
     * @param circleId 圈子ID
     * @param specialRoleType 特殊角色类型
     * @return 角色（如果存在）
     */
    Optional<Role> findByCircleIdAndSpecialRoleType(CircleId circleId, SpecialRoleType specialRoleType);
    
    /**
     * 查找所有特殊角色
     * 
     * @return 特殊角色列表
     */
    List<Role> findByIsSpecialRoleTrue();
    
    /**
     * 查找圈子的所有特殊角色
     * 
     * @param circleId 圈子ID
     * @return 特殊角色列表
     */
    @Query("SELECT r FROM Role r " +
           "WHERE r.circleId = :circleId " +
           "AND r.isSpecialRole = true")
    List<Role> findSpecialRolesByCircleId(@Param("circleId") CircleId circleId);
    
    /**
     * 查找角色及其分配（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 角色ID
     * @return 角色（如果存在）
     */
    @Query("SELECT r FROM Role r " +
           "LEFT JOIN FETCH r.assignments " +
           "WHERE r.id = :id")
    Optional<Role> findByIdWithAssignments(@Param("id") RoleId id);
    
    /**
     * 查找角色及其领域（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 角色ID
     * @return 角色（如果存在）
     */
    @Query("SELECT r FROM Role r " +
           "LEFT JOIN FETCH r.domains " +
           "WHERE r.id = :id")
    Optional<Role> findByIdWithDomains(@Param("id") RoleId id);
    
    /**
     * 查找角色及其所有关联数据（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 角色ID
     * @return 角色（如果存在）
     */
    @Query("SELECT DISTINCT r FROM Role r " +
           "LEFT JOIN FETCH r.assignments " +
           "LEFT JOIN FETCH r.domains " +
           "WHERE r.id = :id")
    Optional<Role> findByIdWithAllRelations(@Param("id") RoleId id);
    
    /**
     * 根据名称和圈子ID查找角色
     * 
     * @param name 角色名称
     * @param circleId 圈子ID
     * @return 角色（如果存在）
     */
    Optional<Role> findByNameAndCircleId(String name, CircleId circleId);
}
