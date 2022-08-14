package com.xiaonicode.filesharing.common.component.handler;

import cn.hutool.core.map.MapUtil;
import com.xiaonicode.filesharing.common.exception.FileSharingException;
import com.xiaonicode.filesharing.common.exception.ValidListGroupException;
import com.xiaonicode.filesharing.common.result.Result;
import com.xiaonicode.filesharing.common.result.ResultStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     *
     * @param e 自定义异常
     * @return 处理结果
     */
    @ExceptionHandler({FileSharingException.class})
    public Result<Void> handleException(FileSharingException e) {
        log.error("Customize error.", e);
        return Result.error(e.getCode(), e.getMsg());
    }

    /**
     * 处理约束违反异常
     *
     * @param e 约束违反异常
     * @return 处理结果
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public Result<Map<Path, String>> handleException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        // 将异常信息收集到 Map (key 表示校验失败的字段, value 表示失败原因)
        Map<Path, String> errorMap = constraintViolations.stream().collect(
                Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage)
        );
        return Result.error(ResultStatus.BAD_REQUEST.getCode(), "数据校验失败", errorMap);
    }

    /**
     * 处理验证异常
     *
     * @param e 验证异常
     * @return 处理结果
     */
    @ExceptionHandler({ValidationException.class})
    public Result<Map<Integer, Map<Path, String>>> handleException(ValidationException e) {
        Map<Integer, Map<Path, String>> errorMap = MapUtil.newHashMap();
        // 强转为自定义异常
        ValidListGroupException exception = (ValidListGroupException) e.getCause();
        exception.getErrors().forEach((key, value) -> {
            // 将异常信息收集到Map (key 表示集合中校验失败元素的索引, value 表示校验失败字段和原因)
            errorMap.put(key, value.stream().collect(
                    Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage)
            ));
        });
        return Result.error(ResultStatus.BAD_REQUEST.getCode(), "集合类型参数分组校验失败", errorMap);
    }

    /**
     * 处理方法参数无效异常
     *
     * @param e 方法参数无效异常
     * @return 处理结果
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<Map<String, String>> handleException(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = MapUtil.newHashMap();
        e.getBindingResult().getFieldErrors().forEach(item -> {
            // 将异常信息收集到Map (key 表示对象实例中校验失败的字段, value 表示校验失败的原因)
            errorMap.put(item.getField(), item.getDefaultMessage());
        });
        return Result.error(ResultStatus.BAD_REQUEST.getCode(), "参数格式校验失败", errorMap);
    }

}
