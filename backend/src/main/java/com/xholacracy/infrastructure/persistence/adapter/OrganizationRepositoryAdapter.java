package com.xholacracy.infrastructure.persistence.adapter;

import com.xholacracy.domain.model.organization.Organization;
import com.xholacracy.domain.model.organization.OrganizationId;
import com.xholacracy.domain.model.organization.OrganizationRepository;
import com.xholacracy.infrastructure.persistence.jpa.OrganizationJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Organization Repository适配器
 * 将Spring Data JPA Repository适配到领域Repository接口
 */
@Component
@Transactional
public class OrganizationRepositoryAdapter implements OrganizationRepository {
    
    private final OrganizationJpaRepository jpaRepository;
    
    public OrganizationRepositoryAdapter(OrganizationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Organization save(Organization organization) {
        return jpaRepository.save(organization);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findById(OrganizationId id) {
        return jpaRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAll() {
        return jpaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findByName(String name) {
        return jpaRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(OrganizationId id) {
        return jpaRepository.existsById(id);
    }
    
    @Override
    public void delete(Organization organization) {
        jpaRepository.delete(organization);
    }
    
    @Override
    public void deleteById(OrganizationId id) {
        jpaRepository.deleteById(id);
    }
}
