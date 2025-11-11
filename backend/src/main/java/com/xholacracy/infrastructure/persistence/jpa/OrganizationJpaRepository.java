package com.xholacracy.infrastructure.persistence.jpa;

import com.xholacracy.domain.model.organization.Organization;
import com.xholacracy.domain.model.organization.OrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Organization的Spring Data JPA Repository
 * 提供基础的CRUD操作和自定义查询
 */
@Repository
public interface OrganizationJpaRepository extends JpaRepository<Organization, OrganizationId> {
    
    /**
     * 根据名称查找组织
     * 
     * @param name 组织名称
     * @return 组织（如果存在）
     */
    Optional<Organization> findByName(String name);
    
    /**
     * 根据名称查找组织（忽略大小写）
     * 
     * @param name 组织名称
     * @return 组织（如果存在）
     */
    Optional<Organization> findByNameIgnoreCase(String name);
    
    /**
     * 查找组织及其Anchor Circle（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 组织ID
     * @return 组织（如果存在）
     */
    @Query("SELECT o FROM Organization o " +
           "LEFT JOIN FETCH o.anchorCircle " +
           "WHERE o.id = :id")
    Optional<Organization> findByIdWithAnchorCircle(@Param("id") OrganizationId id);
}
