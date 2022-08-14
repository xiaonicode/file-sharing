package com.xiaonicode.filesharing.common.config;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Validation 的配置类
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Configuration
public class ValidationConfig {

    @Bean
    public Validator validator() {
        HibernateValidatorConfiguration configuration = Validation
                .byProvider(HibernateValidator.class)
                // 开启快速失败模式
                .configure().failFast(true);

        Validator validator;
        try (ValidatorFactory factory = configuration.buildValidatorFactory()) {
            validator = factory.getValidator();
        }
        return validator;
    }

}
