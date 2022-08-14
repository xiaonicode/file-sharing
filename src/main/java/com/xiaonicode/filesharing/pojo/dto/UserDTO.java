package com.xiaonicode.filesharing.pojo.dto;

import com.xiaonicode.filesharing.common.validation.group.add.AddGroup;
import com.xiaonicode.filesharing.common.validation.group.update.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigInteger;

/**
 * 用户信息的数据传输类
 *
 * @author xiaonicode
 * @createTime 2022-08-11
 */
@Data
public class UserDTO {

    /** 主键 ID */
    @Null(message = "新增用户时, 不能指定 ID", groups = {AddGroup.class})
    @NotNull(message = "修改用户时, 必须指定 ID", groups = {UpdateGroup.class})
    private BigInteger id;

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 昵称 */
    private String nickname;

}
