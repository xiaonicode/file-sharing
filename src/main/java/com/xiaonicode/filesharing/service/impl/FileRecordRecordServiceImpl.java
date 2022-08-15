package com.xiaonicode.filesharing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaonicode.filesharing.common.enums.FileType;
import com.xiaonicode.filesharing.common.result.PageInfo;
import com.xiaonicode.filesharing.mapper.FileRecordMapper;
import com.xiaonicode.filesharing.mapper.UserMapper;
import com.xiaonicode.filesharing.pojo.entity.FileRecordEntity;
import com.xiaonicode.filesharing.pojo.entity.UserEntity;
import com.xiaonicode.filesharing.pojo.query.FileRecordQuery;
import com.xiaonicode.filesharing.pojo.vo.CatalogVO;
import com.xiaonicode.filesharing.pojo.vo.FileRecordVO;
import com.xiaonicode.filesharing.service.CatalogService;
import com.xiaonicode.filesharing.service.FileRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CatalogService catalogService;

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
    public PageInfo<FileRecordVO> getFileRecordPage(FileRecordQuery query) {
        String keyword = query.getKeyword();
        // 封装查询条件
        Wrapper<FileRecordEntity> queryWrapper = new LambdaQueryWrapper<FileRecordEntity>()
                .like(FileRecordEntity::getOriginalFilename, keyword);

        // 分页查询文件记录
        IPage<FileRecordEntity> page = fileRecordMapper
                .selectPage(new Page<>(query.getCurrent(), query.getSize()), queryWrapper);

        // 文件记录实体类 ==> 文件记录视图类
        List<FileRecordVO> vos = fileRecordEntityToView(page.getRecords());

        // 创建并返回分页实例对象
        return new PageInfo<>(vos, page.getTotal(), page.getSize(), page.getCurrent());
    }

    @Override
    public List<FileRecordVO> listFileRecordsByType(String type) {
        FileType fileType = FileType.of(type);
        Wrapper<FileRecordEntity> wrapper = new LambdaQueryWrapper<FileRecordEntity>()
                .likeLeft(FileRecordEntity::getUniqueFilename, fileType.getSuffix());
        // 获取所有文件记录
        List<FileRecordEntity> fileRecords = fileRecordMapper.selectList(wrapper);

        // 文件记录为空, 直接返回空列表
        if (CollectionUtil.isEmpty(fileRecords)) {
            return new ArrayList<>();
        }

        // 文件记录实体类 ==> 文件记录视图类
        return fileRecordEntityToView(fileRecords);
    }

    /**
     * 文件记录实例对象转换 (实体类 ==> 视图类)
     *
     * @param fileRecords 文件记录的实体类实例对象
     * @return 文件记录的视图类实例对象
     */
    private List<FileRecordVO> fileRecordEntityToView(List<FileRecordEntity> fileRecords) {
        return fileRecords.stream().map(entity -> {
            // 属性拷贝, 生成文件记录视图类所对应的实例对象
            FileRecordVO vo = BeanUtil.copyProperties(entity, FileRecordVO.class);

            // 根据创建者 ID, 获取创建者信息
            UserEntity user = userMapper.selectById(entity.getCreatorId());
            // 设置创建者名称
            Optional.ofNullable(user).ifPresent(userEntity ->
                    vo.setUsername(userEntity.getUsername())
            );

            // 根据目录 ID, 获取文件所在的目录路径
            CatalogVO[] catalogPaths = catalogService.listCatalogPaths(entity.getCatalogId());
            // 设置文件所在的目录路径
            vo.setCatalogPaths(catalogPaths);

            return vo;
        }).collect(Collectors.toList());
    }

}
