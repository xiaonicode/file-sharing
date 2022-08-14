package com.xiaonicode.filesharing.common.validation.validator.containsin;

import cn.hutool.core.util.ArrayUtil;
import com.xiaonicode.filesharing.common.validation.constraint.ContainsIn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * 包含整数的校验器
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
public class ContainsInValidatorForInteger implements ConstraintValidator<ContainsIn, Integer> {

    /**
     * 接收包含注解中指定的数组
     */
    private Set<Integer> numbers;

    /**
     * 校验器初始化方法
     *
     * @param constraintAnnotation 校验注解对象
     */
    @Override
    public void initialize(ContainsIn constraintAnnotation) {
        // 得到包含注解的 values 属性值, 获取目标值数组
        int[] values = constraintAnnotation.values();
        if (ArrayUtil.isNotEmpty(values)) {
            numbers = new HashSet<>(values.length);
            // 遍历数组, 将其放入容器
            for (int value : values) {
                numbers.add(value);
            }
        }
    }

    /**
     * 检查整数是否在数组中
     *
     * @param value 待校验整数
     * @param context 约束验证器上下文
     * @return 校验通过返回 true; 否则返回 false
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // 当 value 不为空时, 判断要校验的值是否在注解声明的数组中
        return value == null || numbers.contains(value);
    }

}
