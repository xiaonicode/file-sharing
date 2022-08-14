package com.xiaonicode.filesharing.common.validation.validator.notessential;

import com.xiaonicode.filesharing.common.validation.constraint.NotEssential;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 数组非必要的校验器
 *
 * @author xiaonicode
 * @since 2022-03-16
 */
public class NotEssentialValidatorForArray implements ConstraintValidator<NotEssential, Object[]> {

    /**
     * 检查数组是否为 null 或空元素
     *
     * @param array 待校验数组
     * @param context 约束验证器上下文
     * @return 校验通过返回 true; 否则返回 false
     */
    @Override
    public boolean isValid(Object[] array, ConstraintValidatorContext context) {
        // 当 array 不为空时, 判断要校验的数组是否为 null 或空元素
        return array == null || array.length > 0;
    }

}
