package com.xholacracy.domain.model.proposal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ObjectionCriteria 值对象 - 反对标准
 * 用于验证反对是否有效的四个标准
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectionCriteria {
    
    /**
     * 是否降低圈子能力
     */
    @Column(name = "reduces_capability")
    private Boolean reducesCapability;
    
    /**
     * 是否限制反对者履行职责
     */
    @Column(name = "limits_accountability")
    private Boolean limitsAccountability;
    
    /**
     * 问题是否在没有提案的情况下不存在
     */
    @Column(name = "problem_not_exist_without")
    private Boolean problemNotExistWithout;
    
    /**
     * 是否造成伤害
     */
    @Column(name = "causes_harm")
    private Boolean causesHarm;
    
    /**
     * 创建反对标准
     */
    public static ObjectionCriteria create(boolean reducesCapability, 
                                          boolean limitsAccountability,
                                          boolean problemNotExistWithout, 
                                          boolean causesHarm) {
        return new ObjectionCriteria(
            reducesCapability, 
            limitsAccountability, 
            problemNotExistWithout, 
            causesHarm
        );
    }
    
    /**
     * 检查是否满足任一有效标准
     */
    public boolean isValid() {
        return Boolean.TRUE.equals(reducesCapability) 
            || Boolean.TRUE.equals(limitsAccountability)
            || Boolean.TRUE.equals(problemNotExistWithout) 
            || Boolean.TRUE.equals(causesHarm);
    }
}
