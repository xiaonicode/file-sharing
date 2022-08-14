package com.xiaonicode.filesharing.controller;

import cn.hutool.json.JSONUtil;
import com.xiaonicode.filesharing.common.constant.AuthConstants;
import com.xiaonicode.filesharing.pojo.dto.LoginDTO;
import com.xiaonicode.filesharing.pojo.dto.RegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

/**
 * 用户信息的功能接口的测试类
 *
 * @author xiaonicode
 * @createTime 2022-08-13
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    /** 测试用户注册的功能接口 */
    @Test
    void testRegister() throws Exception {
        // 准备测试数据
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("xiaoni");
        dto.setPassword("1234");
        dto.setNickname("小妮");

        // 构造 POST 请求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtil.toJsonStr(dto))
                .accept(MediaType.APPLICATION_JSON);

        // 发送 POST 请求, 获取响应体
        String content = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
    }

    /** 测试图形验证码的功能接口 */
    @Test
    void testCaptcha() throws Exception {
        // 构造 GET 请求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/captcha")
                .accept(MediaType.APPLICATION_JSON);

        // 发送 GET 请求, 获取响应体
        String content = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
    }

    /** 测试用户登录的功能接口 */
    @Test
    void testLogin() throws Exception {
        // 准备测试数据
        LoginDTO dto = new LoginDTO();
        dto.setUuid("fd6c5b3f97514284a6a9ae2878df40e6");
        dto.setVerifyCode("trk89");
        dto.setUsername("xiaoni");
        dto.setPassword("1234");

        // 构造 POST 请求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtil.toJsonStr(dto))
                .accept(MediaType.APPLICATION_JSON);

        // 发送 POST 请求, 获取响应体
        String content = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
    }

    /** 测试注销登录的功能接口 */
    @Test
    void testLogout() throws Exception {
        // 构造 DELETE 请求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/user/logout")
                .header(AuthConstants.AUTHORIZATION_KEY, AuthConstants.BEARER_PREFIX + "eyJ0eXAiOiJKV1QiLCJleHAiOjE2NjAzNzQ1NTcsImlhdCI6MTY2MDM2Mzc1NywianRpIjoiNmY4YjY4MWMtZmM1Yy00MGE0LTk0OTYtZDRkNmRiY2NjMTc3IiwiYWxnIjoiUlMyNTYifQ.eyJuaWNrbmFtZSI6IuWwj-WmriIsImlkIjoxLCJ1c2VybmFtZSI6InhpYW9uaSJ9.GE3G5sddbBbO6b7dPz-YgT4GwajNs_m9M7YRjZwujjkvy0nZ54vCRyUM0A_2YgNnMl12MxFd6m5gokoE68MS2HA9ISCIu_VxSyu6siiMjgxJn09w6yb1skMoN4mYiNrz1FEpcZIYxlEvU5MIsORk-VLzMhWc-I5Mp7aLp5WMpTrHJNWozfB1tfhADpGqxuZ6oxO7dZx9_01jL4rihuiH3goQNgWTctUx-Tv-rNNlf4wPKnQII2YzLrAfiuzuV3HGwq0GdcRQrNyFtCMq0JMhD8X-wl-8jv8yUKF5dWiO0Dy4MMW00694YFZUznGqm6VG9lpKYPYjhEnXVfV01nxbzg")
                .accept(MediaType.APPLICATION_JSON);

        // 发送 DELETE 请求, 获取响应体
        String content = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
    }

}
