package com.xiaonicode.filesharing.pojo.query;

import lombok.Data;

/**
 * 文件记录的查询条件类
 *
 * @author xiaonicode
 * @createTime 2022-08-14
 */
@Data
public class FileRecordQuery {

    /** 关键字 */
    private String keyword;
    /** 当前页 */
    private Long current = 0L;
    /** 每页记录数 */
    private Long size = 10L;

}
