package com.xiaonicode.filesharing.common.validation.constraint;

import com.xiaonicode.filesharing.common.validation.validator.containsin.ContainsInValidatorForInteger;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 包含注解: 校验某个字段的值是否在一个数组里面
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Documented
@Constraint(validatedBy = {ContainsInValidatorForInteger.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ContainsIn {

    /**
     * 默认提示的消息, 可以指定资源文件中的 key, 也可以直接硬编码
     */
    String message() default "{com.xiaonicode.filesharing.common.validation.constraint.ContainsIn.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 自定义属性, 校验目标字段是否在这个数组中
     */
    int[] values() default {};

}
