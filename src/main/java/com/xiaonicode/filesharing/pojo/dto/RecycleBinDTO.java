package com.xiaonicode.filesharing.pojo.dto;

import com.xiaonicode.filesharing.common.exception.FileSharingException;
import com.xiaonicode.filesharing.common.validation.constraint.ContainsIn;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * 回收站操作的数据传输类
 *
 * @author xiaonicode
 * @createTime 2022-08-13
 */
@Data
public class RecycleBinDTO {

    /** 目录 ID 列表 */
    private List<BigInteger> catalogIds;

    /** 文件记录 ID 列表 */
    private List<BigInteger> fileRecordIds;

    /** 是否已被回收 */
    private Boolean recycled;

    /** 操作类型 (1-回收; 2-还原; 3-删除) */
    @NotNull(message = "操作类型不能为空")
    @ContainsIn(values = {1, 2, 3}, message = "操作类型标识只能为1,2或3")
    private Integer operate;

    /** 操作类型的枚举类 */
    public enum OperateType {
        /** 回收 */
        RECYCLE("Recycle", 1),
        /** 还原 */
        RECOVER("Recover", 2),
        /** 删除 */
        REMOVE("Remove", 3);

        private final String type;
        private final Integer code;

        OperateType(String type, Integer code) {
            this.type = type;
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public Integer getCode() {
            return code;
        }

        public static OperateType of(Integer operate) {
            OperateType[] values = values();
            for (OperateType operateType : values) {
                Integer code = operateType.getCode();
                if (Objects.equals(code, operate)) {
                    return operateType;
                }
            }
            throw new FileSharingException("不支持的操作类型【" + operate + "】");
        }
    }

}
