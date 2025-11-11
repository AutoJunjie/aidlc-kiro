package com.xholacracy.infrastructure.persistence.adapter;

import com.xholacracy.domain.model.circle.Circle;
import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.circle.CircleRepository;
import com.xholacracy.domain.model.organization.OrganizationId;
import com.xholacracy.infrastructure.persistence.jpa.CircleJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Circle Repository适配器
 * 将Spring Data JPA Repository适配到领域Repository接口
 */
@Component
@Transactional
public class CircleRepositoryAdapter implements CircleRepository {
    
    private final CircleJpaRepository jpaRepository;
    
    public CircleRepositoryAdapter(CircleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Circle save(Circle circle) {
        return jpaRepository.save(circle);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Circle> findById(CircleId id) {
        return jpaRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Circle> findAll() {
        return jpaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Circle> findByOrganizationId(OrganizationId organizationId) {
        return jpaRepository.findByOrganizationId(organizationId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Circle> findByParentCircleId(CircleId parentCircleId) {
        return jpaRepository.findByParentCircleId(parentCircleId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Circle> findAnchorCircleByOrganizationId(OrganizationId organizationId) {
        return jpaRepository.findAnchorCircleByOrganizationId(organizationId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(CircleId id) {
        return jpaRepository.existsById(id);
    }
    
    @Override
    public void delete(Circle circle) {
        jpaRepository.delete(circle);
    }
    
    @Override
    public void deleteById(CircleId id) {
        jpaRepository.deleteById(id);
    }
}
