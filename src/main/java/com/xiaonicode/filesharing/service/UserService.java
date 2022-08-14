package com.xiaonicode.filesharing.service;

import com.xiaonicode.filesharing.pojo.dto.LoginDTO;
import com.xiaonicode.filesharing.pojo.dto.RegisterDTO;

/**
 * 用户信息的业务层接口
 *
 * @author xiaonicode
 * @createTime 2022-08-11
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param dto 用户注册的数据传输类的实例对象
     * @return true-注册成功; false-注册失败
     */
    boolean userRegister(RegisterDTO dto);

    /**
     * 用户登录
     *
     * @param dto 用户登录的数据传输类实例
     * @return 登录成功后的令牌信息
     */
    String userLogin(LoginDTO dto);

}
