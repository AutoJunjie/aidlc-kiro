package com.xholacracy.infrastructure.persistence.jpa;

import com.xholacracy.domain.model.partner.Partner;
import com.xholacracy.domain.model.partner.PartnerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Partner的Spring Data JPA Repository
 * 提供基础的CRUD操作和自定义查询
 */
@Repository
public interface PartnerJpaRepository extends JpaRepository<Partner, PartnerId> {
    
    /**
     * 根据邮箱查找伙伴
     * 
     * @param email 邮箱
     * @return 伙伴（如果存在）
     */
    Optional<Partner> findByEmail(String email);
    
    /**
     * 根据邮箱查找伙伴（忽略大小写）
     * 
     * @param email 邮箱
     * @return 伙伴（如果存在）
     */
    Optional<Partner> findByEmailIgnoreCase(String email);
    
    /**
     * 检查邮箱是否已存在
     * 
     * @param email 邮箱
     * @return 如果存在返回true
     */
    boolean existsByEmail(String email);
    
    /**
     * 检查邮箱是否已存在（忽略大小写）
     * 
     * @param email 邮箱
     * @return 如果存在返回true
     */
    boolean existsByEmailIgnoreCase(String email);
    
    /**
     * 根据名称模糊查找伙伴
     * 
     * @param name 名称（支持模糊匹配）
     * @return 伙伴列表
     */
    List<Partner> findByNameContainingIgnoreCase(String name);
    
    /**
     * 查找伙伴及其角色分配（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 伙伴ID
     * @return 伙伴（如果存在）
     */
    @Query("SELECT p FROM Partner p " +
           "LEFT JOIN FETCH p.roleAssignments " +
           "WHERE p.id = :id")
    Optional<Partner> findByIdWithRoleAssignments(@Param("id") PartnerId id);
}
