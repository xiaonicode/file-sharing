package com.xiaonicode.filesharing.common.constant;

/**
 * 全局常量
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
public interface GlobalConstants {

    /** 空白字符 */
    String WHITESPACE = " ";
    /** 空字符串 */
    String EMPTY = "";
    /** 冒号分隔符 */
    String COLON = ":";
    /** 分号分隔符 */
    String SEMICOLON = ";";
    /** 逗号分隔符 */
    String COMMA = ",";
    /** 下划线分隔符 */
    String UNDERSCORE = "_";
    /** 点分隔符 */
    String POINT = ".";
    /** 正斜线分隔符 */
    String SLASH = "/";
    /** http前缀 */
    String HTTP_PREFIX = "http://";
    /** https前缀 */
    String HTTPS_PREFIX = "https://";
    /** 空格的URL编码值: " " ==> "%20" */
    String SPACE_VALUE_BY_URL_ENCODE = "%20";
    /** 浏览器对空格处理后的值: " " ==> "+" */
    String SPACE_VALUE_BY_BROWSER_HANDLE = "+";
    /** 与分隔符 */
    String AND_JOINER = "&";
    /** 键值对连接符 */
    String KEY_VALUE_JOINER = "=";
    /** 问号连接符 */
    String QUES_JOINER = "?";
    /** 默认时间格式 */
    String DEFAULT_DATETIME_PATTERN = "yyyyMMdd";
    /** 默认时区偏移量 */
    String DEFAULT_ZONE_OFFSET = "+8";
    /** 当前用户家目录 */
    String USER_HOME = "user.home";

}
