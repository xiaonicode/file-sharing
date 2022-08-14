package com.xiaonicode.filesharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaonicode.filesharing.pojo.entity.FileRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件记录的持久层接口
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Mapper
public interface FileRecordMapper extends BaseMapper<FileRecordEntity> {

}
