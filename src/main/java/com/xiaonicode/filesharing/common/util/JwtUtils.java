package com.xiaonicode.filesharing.common.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.xiaonicode.filesharing.common.constant.AuthConstants;
import com.xiaonicode.filesharing.common.constant.GlobalConstants;
import com.xiaonicode.filesharing.common.exception.FileSharingException;
import com.xiaonicode.filesharing.common.result.ResultStatus;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Map;

/**
 * 自定义 JWT 工具类
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
public class JwtUtils {

    /**
     * 获取令牌
     * <p>
     * 1. 使用 <a href="https://docs.oracle.com/javase/8/docs/technotes/tools/windows/keytool.html">keytool</a>
     * 进入到当前项目的 resources 目录下, 执行以下命令, 生成用户证书密钥库<br/>
     * <pre>keytool -genkeypair -keyalg "RSA" -sigalg "SHA256withRSA" -dname "CN=Xiao Ni, OU=File Sharing, O=Xiaonicode, L=Chengdu, ST=Sichuan, C=zh_CN" -alias "filesharing" -keypass "123456" -keystore "jwt.jks" -storepass "123456" -validity 30</pre>
     * 2. 参考 <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#KeyStore">密钥库类型</a>
     *
     * @param payload 载荷信息
     * @return 令牌信息
     */
    public static String generateToken(Map<String, Object> payload) {
        // 获取密钥对
        KeyPair keyPair = KeyUtil.getKeyPair(AuthConstants.KEYSTORE_TYPE, FileUtil.getInputStream("jwt.jks"),
                AuthConstants.KEYSTORE_PASSWORD.toCharArray(), AuthConstants.KEYSTORE_ALIAS);

        // 获取 JWT 签名器
        JWTSigner signer = JWTSignerUtil.createSigner(AuthConstants.SIGN_ALG_TYPE, keyPair);

        // 设置 JWT 头信息
        Map<String, Object> headers = MapUtil.newHashMap();
        DateTime issuedTime = DateTime.now();
        DateTime expiresTime = DateTime.of(issuedTime.getTime() + AuthConstants.JWT_EXPIRES_TIME);
        headers.put(JWT.ISSUED_AT, issuedTime);
        headers.put(JWT.EXPIRES_AT, expiresTime);
        headers.put(JWT.JWT_ID, IdUtil.fastUUID());

        // 根据头信息, 载荷以及签名器, 生成令牌
        return JWTUtil.createToken(headers, payload, signer);
    }

    /**
     * 获取 JWT 信息
     */
    public static JWT getJwt() {
        // 获取请求域对象
        HttpServletRequest request = RequestUtils.getHttpServletRequest();

        // 从请求头中获取令牌信息
        String token = request.getHeader(AuthConstants.AUTHORIZATION_KEY);

        // 不是正确的令牌, 不做处理
        if (StrUtil.isBlank(token) || !StrUtil.startWithIgnoreCase(token, AuthConstants.BEARER_PREFIX)) {
            throw new FileSharingException(ResultStatus.TOKEN_INVALID);
        }
        // 当令牌信息格式正确后, 才移除前缀
        token = StrUtil.replaceIgnoreCase(token, AuthConstants.BEARER_PREFIX, GlobalConstants.EMPTY);

        // 解析令牌
        return JWTUtil.parseToken(token);
    }

    /**
     * 获取 JWT 载荷信息中的主体 ID
     */
    public static BigInteger getSubjectId() {
        JSONObject payload = getJwt().getPayloads();
        return payload.get(AuthConstants.SUBJECT_ID_KEY, BigInteger.class);
    }

    /**
     * 获取 JWT 载荷信息中的用户名
     */
    public static String getUsername() {
        JSONObject payload = getJwt().getPayloads();
        return payload.get(AuthConstants.USERNAME_KEY, String.class);
    }

    /**
     * 获取 JWT 载荷信息中的昵称
     */
    public static String getNickname() {
        JSONObject payload = getJwt().getPayloads();
        return payload.get(AuthConstants.USERNAME_KEY, String.class);
    }

}
