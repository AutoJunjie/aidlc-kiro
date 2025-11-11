package com.xholacracy.domain.model.partner;

import java.util.List;
import java.util.Optional;

/**
 * Partner Repository接口
 * 定义伙伴的持久化操作
 */
public interface PartnerRepository {
    
    /**
     * 保存伙伴
     */
    Partner save(Partner partner);
    
    /**
     * 根据ID查找伙伴
     */
    Optional<Partner> findById(PartnerId partnerId);
    
    /**
     * 根据邮箱查找伙伴
     */
    Optional<Partner> findByEmail(String email);
    
    /**
     * 查找所有伙伴
     */
    List<Partner> findAll();
    
    /**
     * 删除伙伴
     */
    void delete(Partner partner);
    
    /**
     * 根据ID删除伙伴
     */
    void deleteById(PartnerId partnerId);
    
    /**
     * 检查伙伴是否存在
     */
    boolean existsById(PartnerId partnerId);
    
    /**
     * 检查邮箱是否已存在
     */
    boolean existsByEmail(String email);
}
