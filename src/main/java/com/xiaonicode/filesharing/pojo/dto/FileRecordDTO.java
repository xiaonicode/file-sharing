package com.xiaonicode.filesharing.pojo.dto;

import com.xiaonicode.filesharing.common.validation.constraint.ContainsIn;
import com.xiaonicode.filesharing.common.validation.group.add.AddGroup;
import com.xiaonicode.filesharing.common.validation.group.update.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigInteger;

/**
 * 文件记录的数据传输类
 *
 * @author xiaonicode
 * @createTime 2022-08-14
 */
@Data
public class FileRecordDTO {

    /** 主键 ID */
    @Null(message = "上传文件时, 不能指定 ID", groups = {AddGroup.class})
    @NotNull(message = "修改文件时, 必须指定 ID", groups = {UpdateGroup.class})
    private BigInteger id;

    /** 目录 ID */
    @NotNull(message = "目录 ID 不能为空")
    @Min(value = 0, message = "目录 ID 必须大于等于 0")
    private BigInteger catalogId;

    /** 原始文件名称 */
    // @NotBlank(message = "文件名称不能为空")
    private String originalFilename;

    /** 唯一文件名称 */
    // @NotBlank(message = "文件 URL不能为空")
    private String uniqueFilename;

    /** 权限 (1-私有; 2-公开) */
    @ContainsIn(values = {1, 2}, message = "权限标识只能为1或2")
    private Integer permission;

    /** 是否已被回收 */
    private Boolean recycled;

}
