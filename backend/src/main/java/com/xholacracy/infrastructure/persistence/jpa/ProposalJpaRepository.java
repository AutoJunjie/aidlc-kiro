package com.xholacracy.infrastructure.persistence.jpa;

import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.Proposal;
import com.xholacracy.domain.model.proposal.ProposalId;
import com.xholacracy.domain.model.proposal.ProposalStatus;
import com.xholacracy.domain.model.proposal.ProposalType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Proposal的Spring Data JPA Repository
 * 提供基础的CRUD操作和自定义查询
 */
@Repository
public interface ProposalJpaRepository extends JpaRepository<Proposal, ProposalId> {
    
    /**
     * 根据圈子ID查找提案
     * 
     * @param circleId 圈子ID
     * @return 提案列表
     */
    List<Proposal> findByCircleId(CircleId circleId);
    
    /**
     * 根据圈子ID分页查找提案
     * 
     * @param circleId 圈子ID
     * @param pageable 分页参数
     * @return 提案分页结果
     */
    Page<Proposal> findByCircleId(CircleId circleId, Pageable pageable);
    
    /**
     * 根据提案人ID查找提案
     * 
     * @param proposerId 提案人ID
     * @return 提案列表
     */
    List<Proposal> findByProposerId(PartnerId proposerId);
    
    /**
     * 根据提案人ID分页查找提案
     * 
     * @param proposerId 提案人ID
     * @param pageable 分页参数
     * @return 提案分页结果
     */
    Page<Proposal> findByProposerId(PartnerId proposerId, Pageable pageable);
    
    /**
     * 根据状态查找提案
     * 
     * @param status 提案状态
     * @return 提案列表
     */
    List<Proposal> findByStatus(ProposalStatus status);
    
    /**
     * 根据状态分页查找提案
     * 
     * @param status 提案状态
     * @param pageable 分页参数
     * @return 提案分页结果
     */
    Page<Proposal> findByStatus(ProposalStatus status, Pageable pageable);
    
    /**
     * 根据提案类型查找提案
     * 
     * @param proposalType 提案类型
     * @return 提案列表
     */
    List<Proposal> findByProposalType(ProposalType proposalType);
    
    /**
     * 根据圈子ID和状态查找提案
     * 
     * @param circleId 圈子ID
     * @param status 提案状态
     * @return 提案列表
     */
    List<Proposal> findByCircleIdAndStatus(CircleId circleId, ProposalStatus status);
    
    /**
     * 根据圈子ID和状态分页查找提案
     * 
     * @param circleId 圈子ID
     * @param status 提案状态
     * @param pageable 分页参数
     * @return 提案分页结果
     */
    Page<Proposal> findByCircleIdAndStatus(CircleId circleId, ProposalStatus status, Pageable pageable);
    
    /**
     * 查找提案及其决策历史（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 提案ID
     * @return 提案（如果存在）
     */
    @Query("SELECT p FROM Proposal p " +
           "LEFT JOIN FETCH p.decisionHistory " +
           "WHERE p.id = :id")
    Optional<Proposal> findByIdWithDecisionHistory(@Param("id") ProposalId id);
    
    /**
     * 查找提案及其所有关联数据（使用JOIN FETCH避免N+1问题）
     * 
     * @param id 提案ID
     * @return 提案（如果存在）
     */
    @Query("SELECT DISTINCT p FROM Proposal p " +
           "LEFT JOIN FETCH p.decisionHistory " +
           "LEFT JOIN FETCH p.questions " +
           "LEFT JOIN FETCH p.reactions " +
           "LEFT JOIN FETCH p.amendments " +
           "LEFT JOIN FETCH p.objections " +
           "LEFT JOIN FETCH p.votes " +
           "WHERE p.id = :id")
    Optional<Proposal> findByIdWithAllRelations(@Param("id") ProposalId id);
    
    /**
     * 根据创建日期范围查找提案
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 提案列表
     */
    @Query("SELECT p FROM Proposal p " +
           "WHERE p.createdDate >= :startDate " +
           "AND p.createdDate <= :endDate " +
           "ORDER BY p.createdDate DESC")
    List<Proposal> findByCreatedDateBetween(@Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
    
    /**
     * 根据标题模糊查找提案
     * 
     * @param title 标题（支持模糊匹配）
     * @return 提案列表
     */
    List<Proposal> findByTitleContainingIgnoreCase(String title);
    
    /**
     * 统计圈子的提案数量
     * 
     * @param circleId 圈子ID
     * @return 提案数量
     */
    long countByCircleId(CircleId circleId);
    
    /**
     * 统计圈子特定状态的提案数量
     * 
     * @param circleId 圈子ID
     * @param status 提案状态
     * @return 提案数量
     */
    long countByCircleIdAndStatus(CircleId circleId, ProposalStatus status);
}
