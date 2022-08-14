package com.xiaonicode.filesharing.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xiaonicode.filesharing.common.result.Result;
import com.xiaonicode.filesharing.common.validation.group.add.AddGroup;
import com.xiaonicode.filesharing.common.validation.group.update.UpdateGroup;
import com.xiaonicode.filesharing.pojo.dto.CatalogDTO;
import com.xiaonicode.filesharing.pojo.dto.RecycleBinDTO;
import com.xiaonicode.filesharing.pojo.entity.CatalogEntity;
import com.xiaonicode.filesharing.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * 目录信息的控制器
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping
    public Result<List<CatalogEntity>> listCatalogs() {
        List<CatalogEntity> entities = catalogService.listCatalogs();
        return Result.ok(entities);
    }

    /**
     * 新增目录
     *
     * @param dto 目录信息的数据传输类的实例对象
     * @return 新增结果
     */
    @PostMapping
    public Result<Boolean> addCatalog(@RequestBody @Validated({AddGroup.class, Default.class}) CatalogDTO dto) {
        // 转换实例对象 (目录数据传输类 ==> 目录实体类)
        CatalogEntity entity = BeanUtil.copyProperties(dto, CatalogEntity.class);
        // 新增目录信息
        boolean isSuccess = catalogService.saveOrUpdateCatalog(entity);
        // 返回新增结果
        return Result.ok(isSuccess);
    }

    /**
     * 修改目录
     *
     * @param dto 目录信息的数据传输类的实例对象
     * @return 修改结果
     */
    @PutMapping
    public Result<Boolean> updateCatalog(@RequestBody @Validated({UpdateGroup.class, Default.class}) CatalogDTO dto) {
        // 转换实例对象 (目录数据传输类 ==> 目录实体类)
        CatalogEntity entity = BeanUtil.copyProperties(dto, CatalogEntity.class);
        // 修改目录信息
        boolean isSuccess = catalogService.saveOrUpdateCatalog(entity);
        // 返回修改结果
        return Result.ok(isSuccess);
    }

    /**
     * 回收站操作: 回收, 还原, 永久删除
     *
     * @param dto 回收站操作的数据传输类的实例对象
     * @return 操作结果
     */
    @PostMapping("/recycle-bin")
    public Result<Boolean> recycleBin(@RequestBody @Validated RecycleBinDTO dto) {
        // 回收站操作
        boolean isSuccess = catalogService.batchRecycleBin(dto);
        // 返回操作结果
        return Result.ok(isSuccess);
    }

}
