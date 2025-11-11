package com.xholacracy.infrastructure.persistence.adapter;

import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.proposal.Proposal;
import com.xholacracy.domain.model.proposal.ProposalId;
import com.xholacracy.domain.model.proposal.ProposalRepository;
import com.xholacracy.domain.model.proposal.ProposalStatus;
import com.xholacracy.infrastructure.persistence.jpa.ProposalJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Proposal Repository适配器
 * 将Spring Data JPA Repository适配到领域Repository接口
 */
@Component
@Transactional
public class ProposalRepositoryAdapter implements ProposalRepository {
    
    private final ProposalJpaRepository jpaRepository;
    
    public ProposalRepositoryAdapter(ProposalJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Proposal save(Proposal proposal) {
        return jpaRepository.save(proposal);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Proposal> findById(ProposalId proposalId) {
        return jpaRepository.findById(proposalId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Proposal> findAll() {
        return jpaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Proposal> findByCircleId(CircleId circleId) {
        return jpaRepository.findByCircleId(circleId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Proposal> findByStatus(ProposalStatus status) {
        return jpaRepository.findByStatus(status);
    }
    
    @Override
    public void delete(Proposal proposal) {
        jpaRepository.delete(proposal);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ProposalId proposalId) {
        return jpaRepository.existsById(proposalId);
    }
}
