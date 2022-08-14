package com.xiaonicode.filesharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaonicode.filesharing.pojo.entity.CatalogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 目录信息的持久层接口
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Mapper
public interface CatalogMapper extends BaseMapper<CatalogEntity> {

}
