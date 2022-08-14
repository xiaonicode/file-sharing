package com.xiaonicode.filesharing.pojo.dto;

import com.xiaonicode.filesharing.common.validation.constraint.NotEssential;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户注册的数据传输类
 *
 * @author xiaonicode
 * @createTime 2022-08-11
 */
@Data
public class RegisterDTO {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 昵称 */
    @NotEssential(message = "昵称只要存在, 就不能为空")
    private String nickname;

}
