package com.xiaonicode.filesharing.common.validation.validator.notessential;

import com.xiaonicode.filesharing.common.validation.constraint.NotEssential;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * 集合非必要的校验器
 *
 * @author xiaonicode
 * @since 2022-03-16
 */
public class NotEssentialValidatorForCollection implements ConstraintValidator<NotEssential, Collection<?>> {

    /**
     * 检查集合是否为 null 或空元素
     *
     * @param collection 待校验集合
     * @param context 约束验证器上下文
     * @return 校验通过返回 true, 否则返回 false
     */
    @Override
    public boolean isValid(Collection<?> collection, ConstraintValidatorContext context) {
        // 当 collection 不为空时, 判断要校验的集合是否为 null 或空元素
        return collection == null || collection.size() > 0;
    }

}
