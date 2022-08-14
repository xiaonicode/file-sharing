package com.xiaonicode.filesharing.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.xiaonicode.filesharing.common.constant.AuthConstants;
import com.xiaonicode.filesharing.common.constant.GlobalConstants;
import com.xiaonicode.filesharing.common.result.Result;
import com.xiaonicode.filesharing.common.util.JwtUtils;
import com.xiaonicode.filesharing.pojo.dto.LoginDTO;
import com.xiaonicode.filesharing.pojo.dto.RegisterDTO;
import com.xiaonicode.filesharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户信息业务的控制器
 *
 * @author xiaonicode
 * @createTime 2022-08-11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户注册
     *
     * @param dto 用户注册的数据传输类的实例对象
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody @Validated RegisterDTO dto) {
        // 进行用户注册
        boolean isSuccess = userService.userRegister(dto);
        // 返回注册结果
        return Result.ok(isSuccess);
    }

    /**
     * 获取图形验证码
     *
     * @return 操作结果
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> captcha() {
        Map<String, String> captchaMap = MapUtil.newHashMap();

        // 生成验证码
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(AuthConstants.CAPTCHA_WIDTH, AuthConstants.CAPTCHA_HEIGHT);
        String captchaText = captcha.getCode();

        // 生成唯一标识
        String uuid = IdUtil.fastSimpleUUID();
        captchaMap.put(AuthConstants.UUID_KEY, uuid);

        // 缓存验证码
        redisTemplate.opsForValue().set(AuthConstants.CAPTCHA_KEY_PREFIX + uuid, captchaText,
                AuthConstants.CAPTCHA_EXPIRATION, TimeUnit.SECONDS);

        // 转换验证码 (文本 ==> 图形)
        String captchaImg = captcha.getImageBase64Data();
        captchaMap.put(AuthConstants.CAPTCHA_KEY, captchaImg);

        return Result.ok(captchaMap);
    }

    /**
     * 用户登录
     *
     * @param dto 用户登录的数据传输类的实例对象
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody @Validated LoginDTO dto) {
        String token = userService.userLogin(dto);
        return Result.ok(token);
    }

    /**
     * 注销登录
     *
     * @return 注销结果
     */
    @DeleteMapping("/logout")
    public Result<Boolean> logout() {
        // 获取 JWT 中的头信息
        JSONObject headers = JwtUtils.getJwt().getHeaders();
        // 获取 JWT 唯一标识
        String jti = headers.getStr(JWT.JWT_ID);
        // 获取 JWT 过期时间戳 (单位: 秒)
        Long expireTime = headers.getLong(JWT.EXPIRES_AT);
        if (Objects.nonNull(expireTime)) {
            // 获取当前时间 (单位: 秒)
            Long currentTime = DateUtil.currentSeconds();
            // 令牌未过期, 添加至缓存作为黑名单限制访问, 缓存时间为令牌过期剩余时间
            if (expireTime > currentTime) {
                redisTemplate.opsForValue().set(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti, GlobalConstants.EMPTY,
                        expireTime - currentTime, TimeUnit.SECONDS);
            } else {
                // 令牌永不过期, 则永久加入黑名单
                redisTemplate.opsForValue().set(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti, GlobalConstants.EMPTY);
            }
        }
        // TODO: 2022/8/12 注销登录逻辑待优化
        return Result.ok(true);
    }

}
