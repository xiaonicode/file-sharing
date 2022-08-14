package com.xiaonicode.filesharing.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiaonicode.filesharing.common.exception.FileSharingException;
import com.xiaonicode.filesharing.common.util.JwtUtils;
import com.xiaonicode.filesharing.mapper.CatalogMapper;
import com.xiaonicode.filesharing.mapper.FileRecordMapper;
import com.xiaonicode.filesharing.pojo.dto.RecycleBinDTO;
import com.xiaonicode.filesharing.pojo.entity.CatalogEntity;
import com.xiaonicode.filesharing.pojo.entity.FileRecordEntity;
import com.xiaonicode.filesharing.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 目录信息的业务层接口的实现类
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Service("catalogService")
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogMapper catalogMapper;
    @Autowired
    private FileRecordMapper fileRecordMapper;

    @Override
    public List<CatalogEntity> listCatalogs() {
        return catalogMapper.selectList(null);
    }

    @Override
    public boolean saveOrUpdateCatalog(CatalogEntity entity) {
        // 检查同一目录层级下目录名称的唯一性
        checkCatalogNameUnique(entity.getCatalogLevel(), entity.getCatalogName());

        int count;
        BigInteger id = entity.getId();
        if (Objects.isNull(id)) {
            // 当目录 ID 为空时, 新增目录
            count = catalogMapper.insert(entity);
        } else {
            // 当目录 ID 不为空时, 修改目录
            count = catalogMapper.updateById(entity);
        }

        return count == 1;
    }

    /**
     * 检查同一目录层级下目录名称的唯一性
     *
     * @param catalogLevel 目录层级
     * @param catalogName 目录名称
     */
    private void checkCatalogNameUnique(Integer catalogLevel, String catalogName) {
        Wrapper<CatalogEntity> wrapper = new LambdaQueryWrapper<CatalogEntity>()
                .eq(CatalogEntity::getCatalogLevel, catalogLevel)
                .eq(CatalogEntity::getCatalogName, catalogName);
        // 根据目录名称, 统计同一层级下的目录数
        Long count = catalogMapper.selectCount(wrapper);

        if (Objects.nonNull(count) && count > 0) {
            throw new FileSharingException("Under the same catalog level, the catalog name is not unique.");
        }
    }

    @Override
    public boolean batchRecycleBin(RecycleBinDTO dto) {
        // 注意: 以下 catalogIds 和 fileRecordIds 均可能包含他人创建的目录, 他人上传的文件
        List<BigInteger> catalogIds = dto.getCatalogIds();
        List<BigInteger> fileRecordIds = dto.getFileRecordIds();

        // 当目录 ID 列表和文件记录 ID 列表均为空时, 直接返回 false
        if (CollectionUtil.isEmpty(catalogIds) && CollectionUtil.isEmpty(fileRecordIds)) {
            return false;
        }

        // 获取当前登录主体用户的 ID
        BigInteger subjectId = JwtUtils.getSubjectId();

        // 判断是否允许回收 (还原) 操作
        Boolean isRecycle = isAllowRecycle(dto.getOperate());
        // recycled: ture-执行回收操作; false-执行还原操作
        Boolean recycled = dto.getRecycled();

        // 获取符合条件的目录信息
        Wrapper<CatalogEntity> catalogQueryWrapper = new LambdaQueryWrapper<CatalogEntity>()
                // 筛选出属于当前登录用户主体的目录
                .eq(CatalogEntity::getCreatorId, subjectId)
                .and(queryWrapper -> {
                    if (isRecycle) {
                        // 当前属于回收 (还原) 操作时, 筛选出未被回收 (还原) 的目录
                        queryWrapper.eq(CatalogEntity::getRecycled, !recycled);
                    } else {
                        // 当前属于删除操作时, 筛选出已被回收的目录
                        queryWrapper.eq(CatalogEntity::getRecycled, true);
                    }
                })
                // 指定所选目录 ID 列表
                .in(CatalogEntity::getId, catalogIds);
        List<CatalogEntity> catalogs = catalogMapper.selectList(catalogQueryWrapper);

        // 获取符合条件的文件记录
        Wrapper<FileRecordEntity> fileRecordQueryWrapper = new LambdaQueryWrapper<FileRecordEntity>()
                // 筛选出属于当前登录用户主体的文件
                .eq(FileRecordEntity::getCreatorId, subjectId)
                .and(queryWrapper -> {
                    if (isRecycle) {
                        // 当前属于回收 (还原) 操作时, 筛选出未被回收 (还原) 的文件
                        queryWrapper.eq(FileRecordEntity::getRecycled, !recycled);
                    } else {
                        // 当前属于删除操作时, 筛选出已被回收的文件
                        queryWrapper.eq(FileRecordEntity::getRecycled, true);
                    }
                })
                // 指定所选文件记录 ID 列表
                .in(FileRecordEntity::getId, fileRecordIds);
        List<FileRecordEntity> fileRecords = fileRecordMapper.selectList(fileRecordQueryWrapper);

        // 回收站操作
        operateRecycleBin(catalogs, fileRecords, isRecycle, recycled);

        return true;
    }

    /**
     * 判断是否允许回收 (还原) 操作
     *
     * @param operate 操作类型 (1-回收; 2-还原; 3-删除)
     * @return true-是; false-否
     */
    private Boolean isAllowRecycle(Integer operate) {
        RecycleBinDTO.OperateType operateType = RecycleBinDTO.OperateType.of(operate);
        switch (operateType) {
            case RECYCLE:
            case RECOVER:
                return true;
            default:
                return false;
        }
    }

    /**
     * 回收站操作
     *
     * @param catalogs 待处理的目录列表
     * @param fileRecords 待处理的文件记录列表
     * @param isRecycle ture-回收 (还原); false-删除
     * @param recycled ture-执行回收操作; false-执行还原操作
     */
    private void operateRecycleBin(List<CatalogEntity> catalogs, List<FileRecordEntity> fileRecords,
                                   Boolean isRecycle, Boolean recycled) {
        if (CollectionUtil.isNotEmpty(catalogs)) {
            catalogs.stream()
                    .filter(Objects::nonNull)
                    .map(CatalogEntity::getId)
                    .forEach(catalogId -> {
                        // 回收站递归操作
                        recursionOperateRecycleBin(catalogId, isRecycle, recycled);

                        // 待递归子目录结束后, 再进行以下操作
                        if (isRecycle) {
                            // 当前属于回收 (还原) 操作时, 回收 (还原) 当前目录
                            CatalogEntity entity = new CatalogEntity();
                            entity.setId(catalogId);
                            entity.setRecycled(recycled);
                            catalogMapper.updateById(entity);
                        } else {
                            // 当前属于删除操作时, 删除当前目录
                            catalogMapper.deleteById(catalogId);
                        }
                    });
        }

        if (CollectionUtil.isNotEmpty(fileRecords)) {
            // 收集待处理的文件记录 ID 列表
            List<BigInteger> fileRecordIds = fileRecords.stream()
                    .filter(Objects::nonNull)
                    .map(FileRecordEntity::getId)
                    .collect(Collectors.toList());

            if (isRecycle) {
                // 当前属于回收 (还原) 操作时, 对所选文件进行回收 (还原)
                Wrapper<FileRecordEntity> fileRecordUpdateWrapper = new LambdaUpdateWrapper<FileRecordEntity>()
                        .in(FileRecordEntity::getId, fileRecordIds)
                        .set(FileRecordEntity::getRecycled, recycled);
                fileRecordMapper.update(new FileRecordEntity(), fileRecordUpdateWrapper);
            } else {
                // 当前属于删除操作时, 删除所选文件
                fileRecordMapper.deleteBatchIds(fileRecordIds);
                // TODO: 2022/8/14 调用文件业务, 真正删除服务器上的文件
            }
        }
    }

    /**
     * 回收站递归操作
     *
     * @param currCatalogId 当前待处理的目录 ID
     * @param isRecycle ture-回收 (还原); false-删除
     * @param recycled ture-执行回收操作; false-执行还原操作
     */
    private void recursionOperateRecycleBin(BigInteger currCatalogId, Boolean isRecycle, Boolean recycled) {
        // 根据当前目录 ID, 获取子目录信息
        Wrapper<CatalogEntity> catalogQueryWrapper = new LambdaQueryWrapper<CatalogEntity>()
                .eq(CatalogEntity::getParentId, currCatalogId);
        List<CatalogEntity> subCatalogs = catalogMapper.selectList(catalogQueryWrapper);

        // 根据当前目录 ID, 获取文件记录
        Wrapper<FileRecordEntity> fileRecordQueryWrapper = new LambdaQueryWrapper<FileRecordEntity>()
                .eq(FileRecordEntity::getCatalogId, currCatalogId);
        List<FileRecordEntity> fileRecords = fileRecordMapper.selectList(fileRecordQueryWrapper);

        // 回收站操作
        operateRecycleBin(subCatalogs, fileRecords, isRecycle, recycled);
    }

}
