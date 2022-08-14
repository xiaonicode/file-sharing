package com.xiaonicode.filesharing.common.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分页结果集
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class PageInfo<T> {

    /** 总记录数 */
    private long total;
    /** 每页记录数 */
    private long size;
    /** 总页数 */
    private long pages;
    /** 当前页数 */
    private long current;
    /** 分页记录列表 */
    private List<T> records;

    /**
     * 构造方法
     *
     * @param records 分页记录列表
     * @param total 总记录数
     * @param size 每页记录数
     * @param current 当前页数
     */
    public PageInfo(List<T> records, long total, long size, long current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
        this.pages = (long) Math.ceil((double) total / size);
    }

}
