package com.xiaonicode.filesharing.common.exception;

import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;

/**
 * 集合类型参数分组校验异常
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Getter
@Setter
public class ValidListGroupException extends RuntimeException {

    private Map<Integer, Set<ConstraintViolation<Object>>> errors;

    public ValidListGroupException(Map<Integer, Set<ConstraintViolation<Object>>> errors) {
        super("集合类型参数分组校验失败");
        this.errors = errors;
    }

}

