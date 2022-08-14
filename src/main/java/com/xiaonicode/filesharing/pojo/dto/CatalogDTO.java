package com.xiaonicode.filesharing.pojo.dto;

import com.xiaonicode.filesharing.common.validation.constraint.ContainsIn;
import com.xiaonicode.filesharing.common.validation.group.add.AddGroup;
import com.xiaonicode.filesharing.common.validation.group.update.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigInteger;

/**
 * 目录信息的数据传输类
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Data
public class CatalogDTO {

    /** 主键 ID */
    @Null(message = "新增目录时, 不能指定ID", groups = {AddGroup.class})
    @NotNull(message = "修改目录时, 必须指定ID", groups = {UpdateGroup.class})
    private BigInteger id;

    /** 目录名称 */
    @NotBlank(message = "目录名称不能为空")
    private String catalogName;

    /** 目录层级 */
    @NotNull(message = "目录层级不能为空")
    @Min(value = 0, message = "目录层级必须大于等于 0")
    private Integer catalogLevel;

    /** 父目录 ID (0 表示为根目录) */
    @NotNull(message = "父目录 ID 不能为空")
    @Min(value = 0, message = "父目录 ID 必须大于等于 0")
    private BigInteger parentId;

    /** 权限 (1-私有; 2-公开) */
    @ContainsIn(values = {1, 2}, message = "权限标识只能为1或2")
    private Integer permission;

    /** 是否已被回收 */
    private Boolean recycled;

}
