package com.xiaonicode.filesharing.common.exception;

import com.xiaonicode.filesharing.common.result.ResultStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常类
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Getter
@Setter
public class FileSharingException extends RuntimeException {

    private String msg;
    private String code;

    public FileSharingException(String msg, String code, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
        this.code = code;
    }

    public FileSharingException(String msg, String code) {
        this(msg, code, null);
    }

    public FileSharingException(String msg, ResultStatus status) {
        this(msg, status.getCode());
    }

    public FileSharingException(String msg) {
        this(msg, ResultStatus.EXECUTION_ERROR.getCode());
    }

    public FileSharingException(String msg, Throwable cause) {
        this(msg, ResultStatus.EXECUTION_ERROR.getCode(), cause);
    }

    public FileSharingException(ResultStatus status) {
        this(status.getMsg(), status.getCode());
    }

    public FileSharingException(ResultStatus status, Throwable cause) {
        this(status.getMsg(), status.getCode(), cause);
    }

    public FileSharingException(Throwable cause) {
        this(null, null, cause);
    }

}
