package com.xiaonicode.filesharing.common.result;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 统一返回结果集
 * <p>
 * ps: 私有化构造方法, 不允许外界直接实例化统一返回结果集.
 *
 * @author xiaonicode
 * @createTime 2022-08-11
 */
@Data
public class Result<T> {

    /** 状态码 */
    private String code;
    /** 提示信息 */
    private String msg;
    /** 响应数据 (data-数据; true-操作成功; false-操作失败) */
    private T data;

    /**
     * 默认构造方法
     */
    private Result() {
    }

    /**
     * 构造方法
     *
     * @param status 结果状态码的枚举类实例
     */
    private Result(ResultStatus status) {
        this(status.getCode(), status.getMsg());
    }

    /**
     * 构造方法
     *
     * @param code 状态码
     * @param msg 提示信息
     */
    private Result(String code, String msg) {
        this(code, msg, null);
    }

    /**
     * 构造方法
     *
     * @param status 结果状态码的枚举类实例
     * @param data 响应数据
     */
    private Result(ResultStatus status, T data) {
        this(status.getCode(), status.getMsg(), data);
    }

    /**
     * 构造方法
     *
     * @param code 状态码
     * @param msg 提示信息
     * @param data 响应数据
     */
    private Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 判断操作是否成功
     *
     * @param result 统一返回结果集
     * @param <T> 响应数据的类型
     * @return true-是; false-否
     */
    public static <T> boolean isSuccess(Result<T> result) {
        return StrUtil.equals(result.getCode(), ResultStatus.OK.getCode());
    }

    /**
     * 响应成功
     *
     * @param data 响应数据
     * @param <T> 响应数据的类型
     * @return 统一返回结果集
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultStatus.OK, data);
    }

    /**
     * 响应成功
     *
     * @param <T> 响应数据的类型
     * @return 统一返回结果集
     */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    /**
     * 响应失败
     *
     * @param code 状态码
     * @param msg 提示信息
     * @param data 响应数据
     * @param <T> 响应数据的类型
     * @return 统一返回结果集
     */
    public static <T> Result<T> error(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    /**
     * 响应失败
     *
     * @param code 状态码
     * @param msg 提示信息
     * @param <T> 响应数据的类型
     * @return 统一返回结果集
     */
    public static <T> Result<T> error(String code, String msg) {
        return new Result<>(code, msg);
    }

    /**
     * 响应失败
     *
     * @param status 结果状态码的枚举类实例
     * @param data 响应数据
     * @param <T> 响应数据的类型
     * @return 统一返回结果集
     */
    public static <T> Result<T> error(ResultStatus status, T data) {
        return new Result<>(status, data);
    }

    /**
     * 响应失败
     *
     * @param status 结果状态码的枚举类实例
     * @param <T> 响应数据的类型
     * @return 统一返回结果集
     */
    public static <T> Result<T> error(ResultStatus status) {
        return new Result<>(status);
    }

    /**
     * 响应失败
     *
     * @param msg 提示信息
     * @param <T> 响应数据的类型
     * @return 统一返回结果集
     */
    public static <T> Result<T> error(String msg) {
        return error(ResultStatus.ERROR.getCode(), msg);
    }

    /**
     * 响应失败
     *
     * @param <T> 响应数据的类型
     * @return 统一返回结果集
     */
    public static <T> Result<T> error() {
        return error(ResultStatus.ERROR);
    }

}
