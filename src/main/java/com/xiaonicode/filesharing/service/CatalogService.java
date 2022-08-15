package com.xiaonicode.filesharing.service;

import com.xiaonicode.filesharing.pojo.dto.RecycleBinDTO;
import com.xiaonicode.filesharing.pojo.entity.CatalogEntity;
import com.xiaonicode.filesharing.pojo.vo.CatalogVO;

import java.math.BigInteger;
import java.util.List;

/**
 * 目录信息的业务层接口
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
public interface CatalogService {

    /**
     * 获取所有目录信息
     *
     * @return 目录信息列表
     */
    List<CatalogEntity> listCatalogs();

    /**
     * 新增 / 修改目录
     *
     * @param entity 目录信息的实体类的实例对象
     * @return true-操作成功; false-操作失败
     */
    boolean saveOrUpdateCatalog(CatalogEntity entity);

    /**
     * 回收站批量操作
     *
     * @param dto 回收站操作的数据传输类的实例对象
     * @return true-操作成功; false-操作失败
     */
    boolean batchRecycleBin(RecycleBinDTO dto);

    /**
     * 根据文件所在的目录 ID, 获取文件所在的目录路径
     *
     * @param catalogId 文件所在的目录 ID
     * @return 文件所在的目录路径
     */
    CatalogVO[] listCatalogPaths(BigInteger catalogId);

}
