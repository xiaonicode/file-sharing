package com.xiaonicode.filesharing.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.SystemPropsUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.Header;
import com.xiaonicode.filesharing.common.constant.FileSharingConstants;
import com.xiaonicode.filesharing.common.constant.GlobalConstants;
import com.xiaonicode.filesharing.common.exception.FileSharingException;
import com.xiaonicode.filesharing.common.result.PageInfo;
import com.xiaonicode.filesharing.common.result.Result;
import com.xiaonicode.filesharing.common.result.ResultStatus;
import com.xiaonicode.filesharing.pojo.entity.FileRecordEntity;
import com.xiaonicode.filesharing.pojo.query.CatalogFileQuery;
import com.xiaonicode.filesharing.pojo.vo.CatalogFileVO;
import com.xiaonicode.filesharing.service.FileRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文件记录的控制器
 *
 * @author xiaonicode
 * @createTime 2022-08-12
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/file")
public class FileRecordController {

    @Autowired
    private FileRecordService fileRecordService;

    /** 上传文件的存放路径 */
    private final String uploadFilePath = SystemPropsUtil.get(GlobalConstants.USER_HOME) +
            File.separator + FileSharingConstants.PROJECT_NAME;

    /**
     * 文件上传
     *
     * @param file 上传文件的实例对象
     * @param catalogId 上传文件时所处位置的目录 ID
     * @return 上传结果
     */
    @PostMapping("/upload")
    public Result<Boolean> uploadFile(@RequestParam MultipartFile file,
                                      @RequestParam(value = "cid", required = false, defaultValue = "0") BigInteger catalogId) {
        // 当文件为空时, 直接返回
        if (file.isEmpty()) {
            return Result.error(ResultStatus.BAD_REQUEST.getCode(), "Uploaded file is empty.");
        }

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 判断同一层级下文件名称是否唯一
        boolean isUnique = fileRecordService.isFilenameUnique(catalogId, originalFilename);
        if (!isUnique) {
            return Result.error(ResultStatus.BAD_REQUEST.getCode(), "Uploaded file name is duplicate.");
        }

        // 原始文件名替换为唯一文件名
        String uniqueFilename = IdUtil.fastSimpleUUID() + "." + FileUtil.getSuffix(originalFilename);

        // 生成文件存放目录
        File dest = FileUtil.mkdir(uploadFilePath);

        try {
            // 转存文件
            file.transferTo(new File(dest, uniqueFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 保存文件记录
        FileRecordEntity entity = new FileRecordEntity();
        entity.setCatalogId(catalogId);
        entity.setOriginalFilename(originalFilename);
        entity.setUniqueFilename(uniqueFilename);
        boolean isSuccess = fileRecordService.saveOrUpdateFileRecord(entity);
        return Result.ok(isSuccess);
    }

    /**
     * 文件下载 (单个或者批量)
     *
     * @param response 响应对象
     * @param ids 待下载文件的 ID 列表
     */
    @PostMapping("/download")
    public void downloadFile(HttpServletResponse response,
                             @RequestBody @NotEmpty(message = "请选择要下载的文件") List<BigInteger> ids) {
        // 获取符合条件的文件记录列表
        List<FileRecordEntity> fileRecords = fileRecordService.listFileRecordsByIds(ids);
        // 文件记录为空, 直接返回
        if (CollectionUtil.isEmpty(fileRecords)) {
            throw new FileSharingException(ResultStatus.BAD_REQUEST.getCode(), "The file to download does not exist.");
        }

        // 收集待下载的文件记录
        List<FileRecordEntity> downloadFiles = fileRecords.stream()
                .filter(Objects::nonNull)
                // 从待下载的文件记录中, 筛选出服务器上存在的文件记录
                .filter(fileRecord -> {
                    String uniqueFilename = fileRecord.getUniqueFilename();
                    File file = new File(uploadFilePath, uniqueFilename);
                    return FileUtil.exist(file);
                }).collect(Collectors.toList());
        // 待下载的文件记录为空, 直接返回
        if (CollectionUtil.isEmpty(downloadFiles)) {
            throw new FileSharingException(ResultStatus.BAD_REQUEST.getCode(), "The file to download does not exist.");
        }

        try (ServletOutputStream out = response.getOutputStream()) {
            response.reset();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.setHeader(Header.PRAGMA.toString(), "no-cache");
            response.setHeader(Header.CACHE_CONTROL.toString(), "no-cache");

            // 获取待下载文件的记录数
            int size = downloadFiles.size();
            if (size == 1) {
                // 单文件下载
                FileRecordEntity fileRecord = downloadFiles.get(0);
                String filename = URLUtil.encode(fileRecord.getOriginalFilename(),
                        StandardCharsets.UTF_8).replace("\\+", "%20");
                response.setHeader(Header.CONTENT_DISPOSITION.toString(), "attachment;filename=" + filename);

                String uniqueFilename = fileRecord.getUniqueFilename();
                File downloadFile = new File(uploadFilePath, uniqueFilename);
                out.write(FileUtil.readBytes(downloadFile));
            } else if (size > 1) {
                // todo 多文件下载
                try (ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(out)) {
                    zipOut.setUseZip64(Zip64Mode.AsNeeded);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param ids 文件记录 ID 列表
     * @return 删除成功
     */
    @DeleteMapping
    public Result<Boolean> deleteFile(@RequestBody @NotEmpty(message = "待删除文件记录 ID 列表不能为空") List<BigInteger> ids) {
        // 获取符合条件的文件记录
        List<FileRecordEntity> fileRecords = fileRecordService.listFileRecordsByIds(ids);
        fileRecords.stream().filter(Objects::nonNull).forEach(fileRecord -> {
            String uniqueFilename = fileRecord.getUniqueFilename();
            File file = new File(uploadFilePath, uniqueFilename);
            if (FileUtil.exist(file)) {
                // 删除服务器上的文件
                boolean isDelete = FileUtil.del(file);
                if (isDelete) {
                    // 删除文件记录
                    fileRecordService.removeFileRecordById(fileRecord.getId());
                }
            }
        });
        return Result.ok();
    }

    /**
     * 公开 (私有) 文件
     *
     * @param fileRecordId 文件记录 ID
     * @param permission 权限 (1-私有; 2-公开)
     * @return 操作结果
     */
    @PostMapping("/open")
    public Result<Boolean> openFile(@RequestParam BigInteger fileRecordId, @RequestParam Integer permission) {
        FileRecordEntity entity = new FileRecordEntity();
        entity.setId(fileRecordId);
        entity.setPermission(permission);
        // 修改文件记录
        boolean isSuccess = fileRecordService.saveOrUpdateFileRecord(entity);
        return Result.ok(isSuccess);
    }

    /**
     * 文件搜索
     *
     * @param query
     * @return 搜索结果
     */
    @GetMapping("/search")
    public Result<PageInfo<CatalogFileVO>> searchFile(@RequestParam CatalogFileQuery query) {
        PageInfo<CatalogFileVO> pageInfo = fileRecordService.getCatalogFilePage(query);
        return Result.ok(pageInfo);
    }

    // 文件浏览

    // 文件分类 (是否涉及排序?)
}
