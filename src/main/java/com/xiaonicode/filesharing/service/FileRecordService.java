package com.xiaonicode.filesharing.service;

import com.xiaonicode.filesharing.common.result.PageInfo;
import com.xiaonicode.filesharing.pojo.entity.FileRecordEntity;
import com.xiaonicode.filesharing.pojo.query.FileRecordQuery;
import com.xiaonicode.filesharing.pojo.vo.FileRecordVO;

import java.math.BigInteger;
import java.util.List;

/**
 * 文件记录的业务层接口
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
public interface FileRecordService {

    /**
     * 判断同一层级下文件名称是否唯一
     *
     * @param catalogId 文件所在目录的 ID
     * @param originalFilename 原始文件名称
     * @return true-唯一; false-不唯一
     */
    boolean isFilenameUnique(BigInteger catalogId, String originalFilename);

    /**
     * 新增 / 修改文件
     *
     * @param entity 文件记录的实体类的实例对象
     * @return true-操作成功; false-操作失败
     */
    boolean saveOrUpdateFileRecord(FileRecordEntity entity);

    /**
     * 根据 ID 列表, 获取文件记录
     *
     * @param ids 文件记录 ID 列表
     * @return 文件记录列表
     */
    List<FileRecordEntity> listFileRecordsByIds(List<BigInteger> ids);

    /**
     * 删除文件记录
     *
     * @param id 文件记录 ID
     * @return true-删除成功; false-删除失败
     */
    boolean removeFileRecordById(BigInteger id);

    /**
     * 分页查询文件记录
     *
     * @param query 文件记录的查询条件类实例对象
     * @return 分页结果
     */
    PageInfo<FileRecordVO> getFileRecordPage(FileRecordQuery query);

    /**
     * 根据文件类型, 获取文件记录
     *
     * @param type 文件类型
     * @return 文件记录列表
     */
    List<FileRecordVO> listFileRecordsByType(String type);

}
