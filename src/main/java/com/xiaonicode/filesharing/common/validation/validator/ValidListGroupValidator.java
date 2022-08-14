package com.xiaonicode.filesharing.common.validation.validator;

import cn.hutool.core.map.MapUtil;
import com.xiaonicode.filesharing.common.exception.ValidListGroupException;
import com.xiaonicode.filesharing.common.validation.constraint.ValidListGroup;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 分组校验集合类型参数的校验器
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
public class ValidListGroupValidator implements ConstraintValidator<ValidListGroup, List<?>> {

    /**
     * 注入校验器
     */
    @Autowired
    private Validator validator;

    /**
     * 自定义注解中指定的校验分组
     */
    private Class<?>[] groups;

    /**
     * 是否开启快速失败模式
     */
    private boolean quickFail;

    /**
     * 校验器初始化方法
     *
     * @param constraintAnnotation 校验注解对象
     */
    @Override
    public void initialize(ValidListGroup constraintAnnotation) {
        // 获取注解上指定的分组
        groups = constraintAnnotation.value();
        quickFail = constraintAnnotation.quickFail();
    }

    /**
     * 校验方法
     *
     * @param list 待校验对象集合
     * @param context 约束验证器上下文
     * @return 校验通过返回 true; 否则返回 false
     */
    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        // 保存校验错误的Map (key-集合中对象的索引下标, value-这个对象所有校验错误字段信息的集合)
        Map<Integer, Set<ConstraintViolation<Object>>> errors = MapUtil.newHashMap();

        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            // 用工具类获取 validator 对象, 进行分组校验, 返回错误结果
            Set<ConstraintViolation<Object>> error = validator.validate(object, groups);
            if (error.size() > 0) {
                // 如果当前校验的对象有属性错误, 保存错误结果
                errors.put(i, error);
                // 如果开启了快速失败, 立即抛出异常, 停止后续校验
                if (quickFail) {
                    throw new ValidListGroupException(errors);
                }
            }
        }

        // 如果集合中至少有 1 个对象校验失败, 那么就抛出校验失败异常, 携带失败信息
        if (errors.size() > 0) {
            throw new ValidListGroupException(errors);
        }
        return true;
    }

}
