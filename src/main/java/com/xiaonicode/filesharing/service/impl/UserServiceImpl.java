package com.xiaonicode.filesharing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaonicode.filesharing.common.constant.AuthConstants;
import com.xiaonicode.filesharing.common.exception.FileSharingException;
import com.xiaonicode.filesharing.common.result.ResultStatus;
import com.xiaonicode.filesharing.common.util.JwtUtils;
import com.xiaonicode.filesharing.mapper.UserMapper;
import com.xiaonicode.filesharing.pojo.dto.RegisterDTO;
import com.xiaonicode.filesharing.pojo.entity.UserEntity;
import com.xiaonicode.filesharing.pojo.dto.LoginDTO;
import com.xiaonicode.filesharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 用户信息的业务层接口的实现类
 *
 * @author xiaonicode
 * @createTime 2022-08-11
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean userRegister(RegisterDTO dto) {
        // 转换实例对象 (用户注册数据传输类 ==> 用户信息实体类)
        UserEntity entity = BeanUtil.copyProperties(dto, UserEntity.class);

        // 检查用户名的唯一性
        checkUsernameUnique(entity.getUsername());

        // 明文密码加密
        String sourcePwd = entity.getPassword();
        String encodePwd = BCrypt.hashpw(sourcePwd);
        entity.setPassword(encodePwd);

        // 保存用户信息
        int count = userMapper.insert(entity);
        return count == 1;
    }

    /**
     * 校验用户名唯一性
     *
     * @param username 用户名
     */
    private void checkUsernameUnique(String username) {
        Wrapper<UserEntity> wrapper = new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUsername, username);
        // 根据用户名, 统计符合条件的用户记录数
        Long count = userMapper.selectCount(wrapper);

        if (Objects.nonNull(count) && count > 0) {
            throw new FileSharingException("Username is not unique.");
        }
    }

    @Override
    public String userLogin(LoginDTO dto) {
        // ======================= ↓↓↓ 1. 验证码校验逻辑 ↓↓↓ ==========================
        // 组装验证码文本的 key
        String captchaTextKey = AuthConstants.CAPTCHA_KEY_PREFIX + dto.getUuid();

        // 从缓存取出正确的验证码, 与用户输入的验证码比对
        String captchaText = (String) redisTemplate.opsForValue().get(captchaTextKey);
        Assert.isTrue(StrUtil.isNotBlank(captchaText),
                () -> new FileSharingException("Verification code has expired."));
        Assert.isTrue(StrUtil.equalsIgnoreCase(dto.getVerifyCode(), captchaText),
                () -> new FileSharingException("Incorrect verification code.", ResultStatus.BAD_REQUEST));

        // 当验证码验证通过后, 删除缓存中的验证码
        redisTemplate.delete(captchaTextKey);
        // ======================= ↑↑↑ 1. 验证码校验逻辑 ↑↑↑ ==========================

        // ========================== ↓↓↓ 2. 登录逻辑 ↓↓↓ ============================
        // 根据用户名, 查询符合条件的用户
        String username = dto.getUsername();
        Wrapper<UserEntity> wrapper = new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUsername, username);
        UserEntity entity = userMapper.selectOne(wrapper);
        Assert.isTrue(Objects.nonNull(entity),
                () -> new FileSharingException("Username [" + username + "] does not exist.", ResultStatus.BAD_REQUEST));

        // 当用户存在后, 进行密码匹配
        boolean match = BCrypt.checkpw(dto.getPassword(), entity.getPassword());
        Assert.isTrue(match, () -> new FileSharingException("The password is incorrect.", ResultStatus.BAD_REQUEST));

        // 当密码成功匹配后, 颁发令牌
        Map<String, Object> payload = MapUtil.newHashMap();
        payload.put(AuthConstants.SUBJECT_ID_KEY, entity.getId());
        payload.put(AuthConstants.USERNAME_KEY, entity.getUsername());
        payload.put(AuthConstants.NICKNAME_KEY, entity.getNickname());
        return JwtUtils.generateToken(payload);
        // ========================== ↑↑↑ 2. 登录逻辑 ↑↑↑ ============================
    }

}
