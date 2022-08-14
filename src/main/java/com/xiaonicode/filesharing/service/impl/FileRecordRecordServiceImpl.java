package com.xiaonicode.filesharing.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaonicode.filesharing.common.result.PageInfo;
import com.xiaonicode.filesharing.mapper.FileRecordMapper;
import com.xiaonicode.filesharing.pojo.entity.FileRecordEntity;
import com.xiaonicode.filesharing.pojo.query.CatalogFileQuery;
import com.xiaonicode.filesharing.pojo.vo.CatalogFileVO;
import com.xiaonicode.filesharing.service.FileRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 文件记录的业务层接口的实现类
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Service("fileService")
public class FileRecordRecordServiceImpl implements FileRecordService {

    @Autowired
    private FileRecordMapper fileRecordMapper;

    @Override
    public boolean isFilenameUnique(BigInteger catalogId, String originalFilename) {
        // 根据文件所在目录的 ID, 以及原始文件名称, 统计符合条件的文件记录数
        Wrapper<FileRecordEntity> wrapper = new LambdaQueryWrapper<FileRecordEntity>()
                .eq(FileRecordEntity::getCatalogId, catalogId)
                .eq(FileRecordEntity::getOriginalFilename, originalFilename);
        Long count = fileRecordMapper.selectCount(wrapper);

        // 判断文件是否未存在当前目录中
        return Objects.isNull(count) || count == 0;
    }

    @Override
    public boolean saveOrUpdateFileRecord(FileRecordEntity entity) {
        int count;
        BigInteger id = entity.getId();
        if (Objects.isNull(id)) {
            // 当文件记录 ID 不存在时, 执行新增操作
            count = fileRecordMapper.insert(entity);
        } else {
            // 当文件记录 ID 存在时, 执行更新操作
            count = fileRecordMapper.updateById(entity);
        }
        return count == 1;
    }

    @Override
    public List<FileRecordEntity> listFileRecordsByIds(List<BigInteger> ids) {
        // 如果文件记录 ID 为空, 直接返回空列表
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }

        // 根据 ID 列表, 获取文件记录
        Wrapper<FileRecordEntity> wrapper = new LambdaQueryWrapper<FileRecordEntity>()
                .in(FileRecordEntity::getId, ids);
        return fileRecordMapper.selectList(wrapper);
    }

    @Override
    public boolean removeFileRecordById(BigInteger id) {
        int count = fileRecordMapper.deleteById(id);
        return count == 1;
    }

    @Override
    public PageInfo<CatalogFileVO> getCatalogFilePage(CatalogFileQuery query) {
        String keyword = query.getKeyword();
        // 封装查询条件
        Wrapper<FileRecordEntity> queryWrapper = new LambdaQueryWrapper<FileRecordEntity>()
                .like(FileRecordEntity::getOriginalFilename, keyword);

        // 分页查询文件记录
        IPage<FileRecordEntity> page = fileRecordMapper
                .selectPage(new Page<>(query.getCurrent(), query.getSize()), queryWrapper);
        List<FileRecordEntity> records = page.getRecords();
        return null;
    }

}
