package com.xiaonicode.filesharing.common.result;

/**
 * 结果状态码的枚举类
 *
 * @author xiaonicode
 * @createTime 2022-08-11
 */
public enum ResultStatus {
    /** 操作成功 */
    OK("00200", "Success"),
    /** 操作失败 */
    ERROR("00500", "Failure"),
    /** 请求错误 */
    BAD_REQUEST("00400", "Request error."),
    /** 没有权限 */
    UNAUTHORIZED("00401", "No permission."),
    /** 访问被禁止 */
    FORBIDDEN("00403", "Access forbidden."),
    /** 未找到请求资源 */
    NOT_FOUND("00404", "Not found"),
    /** 令牌无效或已过期 */
    TOKEN_INVALID("A3001", "The token is invalid or expired."),
    /** 系统执行出错 */
    EXECUTION_ERROR("B0001", "System execution error.");

    private final String code;
    private final String msg;

    ResultStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
