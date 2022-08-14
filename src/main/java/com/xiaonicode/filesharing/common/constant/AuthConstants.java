package com.xiaonicode.filesharing.common.constant;

/**
 * 认证与授权常量
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
public interface AuthConstants {

    /** UUID 的 key */
    String UUID_KEY = "uuid";
    /** 图形验证码宽度 */
    int CAPTCHA_WIDTH = 200;
    /** 图形验证码高度 */
    int CAPTCHA_HEIGHT = 50;
    /** 验证码的 key 前缀 */
    String CAPTCHA_KEY_PREFIX = "captcha:";
    /** 验证码的过期时长 (单位: 秒) */
    long CAPTCHA_EXPIRATION = 2 * 60;
    /** 验证码的 key */
    String CAPTCHA_KEY = "captcha";
    /** 密钥库类型 */
    String KEYSTORE_TYPE = "jks";
    /** 密钥库别名 */
    String KEYSTORE_ALIAS = "filesharing";
    /** 密钥库口令 */
    String KEYSTORE_PASSWORD = "123456";
    /** 签名算法类型 */
    String SIGN_ALG_TYPE = "RS256";
    /** 主体 ID 的 key */
    String SUBJECT_ID_KEY = "id";
    /** 用户名的 key */
    String USERNAME_KEY = "username";
    /** 昵称的 key */
    String NICKNAME_KEY = "nickname";
    /** 认证请求头的 key */
    String AUTHORIZATION_KEY = "Authorization";
    /** Bearer 认证前缀 */
    String BEARER_PREFIX = "Bearer ";
    /** 黑名单令牌前缀 */
    String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";
    /** JWT 过期时间 (单位: 毫秒) */
    long JWT_EXPIRES_TIME = 3 * 60 * 60 * 1000;

}
