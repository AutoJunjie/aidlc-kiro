package com.xholacracy.infrastructure.persistence.adapter;

import com.xholacracy.domain.model.circle.CircleId;
import com.xholacracy.domain.model.role.Role;
import com.xholacracy.domain.model.role.RoleId;
import com.xholacracy.domain.model.role.RoleRepository;
import com.xholacracy.infrastructure.persistence.jpa.RoleJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Role Repository适配器
 * 将Spring Data JPA Repository适配到领域Repository接口
 */
@Component
@Transactional
public class RoleRepositoryAdapter implements RoleRepository {
    
    private final RoleJpaRepository jpaRepository;
    
    public RoleRepositoryAdapter(RoleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Role save(Role role) {
        return jpaRepository.save(role);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(RoleId roleId) {
        return jpaRepository.findById(roleId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return jpaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> findByCircleId(CircleId circleId) {
        return jpaRepository.findByCircleId(circleId);
    }
    
    @Override
    public void delete(Role role) {
        jpaRepository.delete(role);
    }
    
    @Override
    public void deleteById(RoleId roleId) {
        jpaRepository.deleteById(roleId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(RoleId roleId) {
        return jpaRepository.existsById(roleId);
    }
}
