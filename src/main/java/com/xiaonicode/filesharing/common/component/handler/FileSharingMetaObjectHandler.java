package com.xiaonicode.filesharing.common.component.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xiaonicode.filesharing.common.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 自定义 MetaObjectHandler 的实现类
 *
 * @author xiaonicode
 * @createTime 2022-08-11
 */
@Slf4j
@Component
public class FileSharingMetaObjectHandler implements MetaObjectHandler {

    /** 当前记录被首次创建时, 自动填充创建时间和创建者 ID */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        // 将当前登录主体用户 ID 设置为创建者 ID
        this.strictInsertFill(metaObject, "creatorId", JwtUtils::getSubjectId, BigInteger.class);
    }

    /** 当前记录被修改时, 自动填充修改时间 */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }

}
