package com.xiaonicode.filesharing.common.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.util.Map;

/**
 * 自定义 Map 工具类
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
public class FileSharingMapUtils {

    /**
     * 将 Map 的 key 转换为下划线, val 转换为字符串
     *
     * @param sourceMap 源 Map 对象
     * @param <S> 源 Map 对象类型
     * @return 目标 Map 对象
     */
    public static <S> Map<String, String> toUnderlineCase(S sourceMap) {
        Map<String, String> targetMap = MapUtil.newHashMap();
        JSONUtil.parseObj(sourceMap).forEach((key, val) -> {
            String targetKey = StrUtil.toUnderlineCase(key);
            String targetVal = StrUtil.toString(val);
            targetMap.put(targetKey, targetVal);
        });
        return targetMap;
    }

    /**
     * 将 Map 的 key 转换为下划线
     *
     * @param sourceMap 源 Map 对象
     * @param <S> 源 Map 对象类型
     * @return 目标 Map 对象
     */
    public static <S> Map<String, Object> toUnderlineCaseKey(S sourceMap) {
        Map<String, Object> targetMap = MapUtil.newHashMap();
        JSONUtil.parseObj(sourceMap).forEach((key, val) -> {
            key = StrUtil.toUnderlineCase(key);
            targetMap.put(key, val);
        });
        return targetMap;
    }

    /**
     * 将 Map 的 key 转换为小驼峰
     *
     * @param sourceMap 源 Map 对象
     * @param <S> 源 Map 对象类型
     * @return 目标 Map 对象
     */
    public static <S> Map<String, Object> toCamelCaseKey(S sourceMap) {
        Map<String, Object> targetMap = MapUtil.newHashMap();
        JSONUtil.parseObj(sourceMap).forEach((key, val) -> {
            key = StrUtil.toCamelCase(key);
            targetMap.put(key, val);
        });
        return targetMap;
    }

}
