package com.xiaonicode.filesharing.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录的数据传输类
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Data
public class LoginDTO {

    /** UUID */
    @NotBlank(message = "UUID 不能为空")
    private String uuid;

    /** 验证码 */
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;

}
