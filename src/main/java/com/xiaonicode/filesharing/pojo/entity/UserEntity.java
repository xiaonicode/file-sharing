package com.xiaonicode.filesharing.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 用户信息的实体类
 *
 * @author xiaonicode
 * @createTime 2022-08-10
 */
@Data
@TableName("tb_user")
public class UserEntity {

    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private BigInteger id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 昵称 */
    private String nickname;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /** 是否已被逻辑删除 (0-否; 1-是) */
    @TableField("is_deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

}
