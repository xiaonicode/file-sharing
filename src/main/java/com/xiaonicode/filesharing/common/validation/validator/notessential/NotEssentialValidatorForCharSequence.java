package com.xiaonicode.filesharing.common.validation.validator.notessential;

import com.xiaonicode.filesharing.common.validation.constraint.NotEssential;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符序列非必要的校验器
 *
 * @author xiaonicode
 * @since 2022-03-16
 */
public class NotEssentialValidatorForCharSequence implements ConstraintValidator<NotEssential, CharSequence> {

    /**
     * 检查字符序列是否为 null 或空串
     *
     * @param charSequence 待校验字符序列
     * @param context 约束验证器上下文
     * @return 校验通过返回 true; 否则返回 false
     */
    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        // 当 charSequence 不为空时, 判断要校验的字符序列是否为 null 或空串
        return charSequence == null || charSequence.toString().trim().length() > 0;
    }

}
