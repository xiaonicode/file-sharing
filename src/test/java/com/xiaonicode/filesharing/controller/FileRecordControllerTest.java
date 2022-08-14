package com.xiaonicode.filesharing.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.xiaonicode.filesharing.common.constant.AuthConstants;
import com.xiaonicode.filesharing.pojo.dto.FileRecordDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件记录的功能接口的测试类
 *
 * @author xiaonicode
 * @createTime 2022-08-14
 */
@SpringBootTest
@AutoConfigureMockMvc
class FileRecordControllerTest {

    @Autowired
    MockMvc mockMvc;

    /** 测试文件上传的功能接口 */
    @Test
    void testUploadFile() throws Exception {
        // 准备测试数据
        FileRecordDTO dto = new FileRecordDTO();
        dto.setId(BigInteger.ONE);
        dto.setOriginalFilename("a.txt");
        dto.setUniqueFilename("http://127.0.0.1:8197/a.txt");
        dto.setCatalogId(BigInteger.ZERO);
        dto.setPermission(1);
        byte[] fileBytes = IoUtil.readBytes(FileUtil.getInputStream("D:\\Downloads\\a.txt"));

        // 构造 POST 请求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/file/upload")
                .header(AuthConstants.AUTHORIZATION_KEY, AuthConstants.BEARER_PREFIX + "eyJ0eXAiOiJKV1QiLCJleHAiOjE2NjA0MDYwMDYsImlhdCI6MTY2MDM5NTIwNiwianRpIjoiZGY1NWI1ZTgtOGMzNi00M2JmLTg3NjYtY2Q5NmZiMjhkNWQ3IiwiYWxnIjoiUlMyNTYifQ.eyJuaWNrbmFtZSI6IuWwj-WmriIsImlkIjoxLCJ1c2VybmFtZSI6InhpYW9uaSJ9.cp3DS1bFCeDWncF6e94W4xgypJ7Cz-OsBRuwhUEFQ3JsK3XHkioejXD43peovQiCK39VcuFv2gedfacCvyiF5YbOswAd2TrClCIDUMmAKonS5vpnoMUsn_d3v5oZQjrgO10MhWKG5iACRaJE7UtNkEJc6vWArFVDBq9DF-GwY_BeBjKqER3LD-BKqQLGKiL_D5uw4UaNQaWLEM0kQib4g9w74m95Fje0ltWLmtKPhUnGKAIJuUzJlYVHd77J5wSrrweXJU1dNdUEXXOUAT135DvcFmeByQHmS0r5u1Zl3mK5n8ZrcoElTjfG40FNc1iXItH_-XOWsYUNhJjCfFb3qQ")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(fileBytes)
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

    /** 测试文件下载的功能接口 */
    @Test
    void testDownloadFile() throws Exception {
        // 准备测试数据
        List<BigInteger> ids = new ArrayList<>();
        ids.add(BigInteger.valueOf(11));

        // 构造 POST 请求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/file/download")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtil.toJsonStr(ids))
                .accept(MediaType.APPLICATION_JSON);

        // 发送 POST 请求, 获取响应体
        String content = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
    }

    /** 测试删除文件的功能接口 */
    @Test
    void testDeleteFile() throws Exception {
        // 准备测试数据
        List<BigInteger> ids = new ArrayList<>();
        ids.add(BigInteger.valueOf(11));

        // 构造 DELETE 请求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/file")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtil.toJsonStr(ids))
                .accept(MediaType.APPLICATION_JSON);

        // 发送 DELETE 请求, 获取响应体
        String content = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
    }

    /** 测试公开 (私有) 文件的功能接口 */
    @Test
    void testOpenFile() throws Exception {
        // 构造 DELETE 请求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/file")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fileRecordId", "12")
                .param("permission", "2")
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
