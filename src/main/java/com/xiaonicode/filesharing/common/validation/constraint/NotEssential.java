package com.xiaonicode.filesharing.common.validation.constraint;

import com.xiaonicode.filesharing.common.validation.validator.notessential.NotEssentialValidatorForArray;
import com.xiaonicode.filesharing.common.validation.validator.notessential.NotEssentialValidatorForCharSequence;
import com.xiaonicode.filesharing.common.validation.validator.notessential.NotEssentialValidatorForCollection;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 属性字段非必要的注解
 * <p>
 * 支持的类型有: CharSequence, Array, Collection
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Documented
@Constraint(validatedBy = {
        NotEssentialValidatorForArray.class,
        NotEssentialValidatorForCharSequence.class,
        NotEssentialValidatorForCollection.class
})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(NotEssential.List.class)
public @interface NotEssential {

    /**
     * 默认提示的消息, 可以指定资源文件中的 key, 也可以直接硬编码
     */
    String message() default "{com.xiaonicode.filesharing.common.validation.constraint.NotEssential.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @interface List {

        /**
         * 自定义属性, 校验目标字段是否为空
         */
        NotEssential[] value();

    }

}
