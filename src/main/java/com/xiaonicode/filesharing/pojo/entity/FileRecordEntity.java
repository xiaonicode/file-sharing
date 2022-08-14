package com.xiaonicode.filesharing.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 文件记录的实体类
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Data
@TableName("tb_file_record")
public class FileRecordEntity {

    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private BigInteger id;

    /** 目录 ID */
    private BigInteger catalogId;

    /** 原始文件名称 */
    private String originalFilename;

    /** 唯一文件名称 */
    private String uniqueFilename;

    /** 权限 (1-私有; 2-公开) */
    private Integer permission;

    /** 是否已被回收 */
    @TableField("is_recycled")
    private Boolean recycled;

    /** 创建者 ID */
    @TableField(fill = FieldFill.INSERT)
    private BigInteger creatorId;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /** 是否已被逻辑删除 (0-否; 1-是) */
    @TableField("is_deleted")
    private Integer deleted;

}
