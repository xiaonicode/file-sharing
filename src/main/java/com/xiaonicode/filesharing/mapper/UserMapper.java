package com.xiaonicode.filesharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaonicode.filesharing.pojo.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息的持久层接口
 *
 * @author xiaonicode
 * @createTime 2022-08-11
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
