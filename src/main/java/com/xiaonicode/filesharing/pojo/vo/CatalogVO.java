package com.xiaonicode.filesharing.pojo.vo;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 目录信息的视图类
 *
 * @author xiaonicode
 * @createTime 2022-08-14
 */
@Data
public class CatalogVO {

    private BigInteger id;

    private String catalogName;

    private Integer catalogLevel;

    private BigInteger parentId;

    private Integer permission;

    private Boolean recycled;

    private BigInteger creatorId;

    private String username;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
