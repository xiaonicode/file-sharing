package com.xiaonicode.filesharing.common.enums;

import cn.hutool.core.util.StrUtil;
import com.xiaonicode.filesharing.common.exception.FileSharingException;

/**
 * 文件类型的枚举类
 *
 * @author xiaonicode
 * @createTime 2022-08-15
 */
public enum FileType {
    /** 文本 */
    TXT("text", ".txt"),
    // TODO ...
    ;

    private final String type;
    private final String suffix;

    FileType(String type, String suffix) {
        this.type = type;
        this.suffix = suffix;
    }

    public String getType() {
        return type;
    }

    public String getSuffix() {
        return suffix;
    }

    public static FileType of(String type) {
        FileType[] values = values();
        for (FileType fileType : values) {
            if (StrUtil.equalsIgnoreCase(fileType.getType(), type)) {
                return fileType;
            }
        }
        throw new FileSharingException("Unsupported file type【" + type + "】");
    }
}
