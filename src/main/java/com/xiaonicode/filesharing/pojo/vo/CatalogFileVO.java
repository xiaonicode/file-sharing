package com.xiaonicode.filesharing.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 文件目录的视图类
 *
 * @author xiaonicode
 * @createTime 2022-08-14
 */
@Data
public class CatalogFileVO {

    /** 目录列表 */
    private List<CatalogVO> catalogs;
    /** 文件记录列表 */
    private List<FileRecordVO> fileRecords;

}
