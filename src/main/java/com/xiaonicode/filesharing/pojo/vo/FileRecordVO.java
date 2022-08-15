package com.xiaonicode.filesharing.pojo.vo;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 文件记录的视图类
 *
 * @author xiaonicode
 * @createTime 2022-08-14
 */
@Data
public class FileRecordVO {

    private BigInteger id;

    private BigInteger catalogId;

    /** 文件所在的目录路径 */
    private CatalogVO[] catalogPaths;

    private String originalFilename;

    private Integer permission;

    private Boolean recycled;

    private BigInteger creatorId;

    /** 创建者名称 */
    private String username;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
