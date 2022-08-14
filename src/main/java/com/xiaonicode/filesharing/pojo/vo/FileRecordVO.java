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

    private String catalogName;

    private String originalFilename;

    private Integer permission;

    private Boolean recycled;

    private BigInteger creatorId;

    private String username;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
