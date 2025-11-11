package com.xholacracy.domain.service;

import com.xholacracy.domain.model.proposal.Objection;
import com.xholacracy.domain.model.proposal.ObjectionCriteria;

/**
 * 反对验证领域服务
 * 负责验证反对意见是否符合 Holacracy 的四个有效反对标准
 */
public class ObjectionValidationService {
    
    /**
     * 验证反对意见是否有效
     * 
     * 根据 Holacracy 规则，有效的反对必须满足以下四个标准之一：
     * 1. 减少圈子能力 (Reduces Circle Capability)
     * 2. 限制反对者履行职责 (Limits Objector's Accountability Fulfillment)
     * 3. 问题不存在于没有提案的情况下 (Problem Does Not Exist Without Proposal)
     * 4. 造成伤害 (Causes Harm)
     * 
     * @param objection 反对意见
     * @return 反对是否有效
     */
    public boolean validate(Objection objection) {
        if (objection == null) {
            return false;
        }
        
        ObjectionCriteria criteria = objection.getCriteria();
        if (criteria == null) {
            return false;
        }
        
        // 检查是否至少满足一个有效反对标准
        return Boolean.TRUE.equals(criteria.getReducesCapability()) ||
               Boolean.TRUE.equals(criteria.getLimitsAccountability()) ||
               Boolean.TRUE.equals(criteria.getProblemNotExistWithout()) ||
               Boolean.TRUE.equals(criteria.getCausesHarm());
    }
    
    /**
     * 验证反对意见并提供详细的验证结果
     * 
     * @param objection 反对意见
     * @return 验证结果，包含是否有效和满足的标准
     */
    public ObjectionValidationResult validateWithDetails(Objection objection) {
        if (objection == null) {
            return new ObjectionValidationResult(false, "Objection is null");
        }
        
        ObjectionCriteria criteria = objection.getCriteria();
        if (criteria == null) {
            return new ObjectionValidationResult(false, "Objection criteria is null");
        }
        
        boolean isValid = validate(objection);
        
        if (!isValid) {
            return new ObjectionValidationResult(
                false,
                "Objection does not meet any of the four valid objection criteria"
            );
        }
        
        // 构建满足的标准列表
        StringBuilder metCriteria = new StringBuilder("Objection meets the following criteria: ");
        boolean first = true;
        
        if (Boolean.TRUE.equals(criteria.getReducesCapability())) {
            metCriteria.append("Reduces Circle Capability");
            first = false;
        }
        
        if (Boolean.TRUE.equals(criteria.getLimitsAccountability())) {
            if (!first) metCriteria.append(", ");
            metCriteria.append("Limits Objector's Accountability Fulfillment");
            first = false;
        }
        
        if (Boolean.TRUE.equals(criteria.getProblemNotExistWithout())) {
            if (!first) metCriteria.append(", ");
            metCriteria.append("Problem Does Not Exist Without Proposal");
            first = false;
        }
        
        if (Boolean.TRUE.equals(criteria.getCausesHarm())) {
            if (!first) metCriteria.append(", ");
            metCriteria.append("Causes Harm");
        }
        
        return new ObjectionValidationResult(true, metCriteria.toString());
    }
    
    /**
     * 检查反对意见是否满足特定标准
     * 
     * @param objection 反对意见
     * @param criteriaType 标准类型
     * @return 是否满足该标准
     */
    public boolean meetsCriteria(Objection objection, ObjectionCriteriaType criteriaType) {
        if (objection == null || objection.getCriteria() == null) {
            return false;
        }
        
        ObjectionCriteria criteria = objection.getCriteria();
        
        return switch (criteriaType) {
            case REDUCES_CAPABILITY -> Boolean.TRUE.equals(criteria.getReducesCapability());
            case LIMITS_ACCOUNTABILITY -> Boolean.TRUE.equals(criteria.getLimitsAccountability());
            case PROBLEM_NOT_EXIST_WITHOUT -> Boolean.TRUE.equals(criteria.getProblemNotExistWithout());
            case CAUSES_HARM -> Boolean.TRUE.equals(criteria.getCausesHarm());
        };
    }
    
    /**
     * 反对验证结果
     */
    public static class ObjectionValidationResult {
        private final boolean valid;
        private final String message;
        
        public ObjectionValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    /**
     * 反对标准类型枚举
     */
    public enum ObjectionCriteriaType {
        /**
         * 减少圈子能力
         */
        REDUCES_CAPABILITY,
        
        /**
         * 限制反对者履行职责
         */
        LIMITS_ACCOUNTABILITY,
        
        /**
         * 问题不存在于没有提案的情况下
         */
        PROBLEM_NOT_EXIST_WITHOUT,
        
        /**
         * 造成伤害
         */
        CAUSES_HARM
    }
}
