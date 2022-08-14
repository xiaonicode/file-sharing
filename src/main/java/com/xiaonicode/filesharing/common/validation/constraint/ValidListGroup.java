package com.xiaonicode.filesharing.common.validation.constraint;

import com.xiaonicode.filesharing.common.validation.validator.ValidListGroupValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.groups.Default;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * 集合类型参数的分组校验注解
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Documented
@Constraint(validatedBy = {ValidListGroupValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidListGroup {

    String message() default "分组校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 默认属性, 代表对集合类型的数据采用哪个分组进行校验. 默认值: Default.class
     */
    Class<?>[] value() default {Default.class};

    /**
     * 是否开启快速失败模式. 默认值: false
     */
    boolean quickFail() default false;

}
