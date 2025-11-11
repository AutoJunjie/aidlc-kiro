package com.xholacracy.infrastructure.persistence.adapter;

import com.xholacracy.domain.model.partner.Partner;
import com.xholacracy.domain.model.partner.PartnerId;
import com.xholacracy.domain.model.partner.PartnerRepository;
import com.xholacracy.infrastructure.persistence.jpa.PartnerJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Partner Repository适配器
 * 将Spring Data JPA Repository适配到领域Repository接口
 */
@Component
@Transactional
public class PartnerRepositoryAdapter implements PartnerRepository {
    
    private final PartnerJpaRepository jpaRepository;
    
    public PartnerRepositoryAdapter(PartnerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Partner save(Partner partner) {
        return jpaRepository.save(partner);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Partner> findById(PartnerId partnerId) {
        return jpaRepository.findById(partnerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Partner> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Partner> findAll() {
        return jpaRepository.findAll();
    }
    
    @Override
    public void delete(Partner partner) {
        jpaRepository.delete(partner);
    }
    
    @Override
    public void deleteById(PartnerId partnerId) {
        jpaRepository.deleteById(partnerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(PartnerId partnerId) {
        return jpaRepository.existsById(partnerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
