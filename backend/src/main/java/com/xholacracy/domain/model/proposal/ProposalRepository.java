package com.xholacracy.domain.model.proposal;

import com.xholacracy.domain.model.circle.CircleId;

import java.util.List;
import java.util.Optional;

/**
 * Proposal Repository 接口
 */
public interface ProposalRepository {
    
    /**
     * 保存提案
     */
    Proposal save(Proposal proposal);
    
    /**
     * 根据 ID 查找提案
     */
    Optional<Proposal> findById(ProposalId id);
    
    /**
     * 查找所有提案
     */
    List<Proposal> findAll();
    
    /**
     * 根据圈子 ID 查找提案
     */
    List<Proposal> findByCircleId(CircleId circleId);
    
    /**
     * 根据状态查找提案
     */
    List<Proposal> findByStatus(ProposalStatus status);
    
    /**
     * 删除提案
     */
    void delete(Proposal proposal);
    
    /**
     * 检查提案是否存在
     */
    boolean existsById(ProposalId id);
}
