package com.xiaonicode.filesharing.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.SystemPropsUtil;
import cn.hutool.http.Header;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 自定义文件下载的工具类
 *
 * @author xiaonicode
 * @createTime 2022-08-15
 */
@Slf4j
public class ZipDownloadUtils {

    /** 获取当前系统的临时目录 */
    private static final String FILE_PATH = SystemPropsUtil.get("java.io.tmpdir") + File.separator;
    private static final int BUFFER_SIZE = 1024;
    private static final int ZIP_BUFFER_SIZE = 8192;

    /**
     * ZIP 打包下载
     *
     * @param response 响应对象
     * @param zipFilename ZIP 包名称
     * @param files 待下载的文件列表
     */
    public static void zipDownload(HttpServletResponse response, String zipFilename, List<File> files) {
        // ZIP 文件路径
        String zipPath = FILE_PATH + zipFilename;

        try (FileOutputStream fos = new FileOutputStream(zipPath)) {
            // 创建 ZIP 输出流
            try (ZipOutputStream out = new ZipOutputStream(fos)) {
                // 声明文件集合, 用于存放文件
                byte[] buffer = new byte[BUFFER_SIZE];
                // 将文件放入 ZIP 压缩包
                for (File file : files) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        // 将指定文件名称的条目存放到 ZIP 输出流
                        out.putNextEntry(new ZipEntry(file.getName()));
                        int len;
                        // 读入需要下载的文件的内容, 打包到 ZIP 文件
                        while ((len = fis.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                        // 写完一次, 就手动关闭当前条目
                        out.closeEntry();
                    }
                }
            }
            // 下载 ZIP 文件
            downloadFile(response, zipFilename);
        } catch (IOException e) {
            log.error("File download error.", e);
        } finally {
            // ZIP 文件也删除
            files.add(new File(zipPath));
            deleteFile(files);
        }
    }

    /**
     * 下载文件
     *
     * @param response 响应对象
     * @param zipFilename ZIP 包名称
     */
    private static void downloadFile(HttpServletResponse response, String zipFilename) {
        String zipPath = FILE_PATH + zipFilename;
        File file = new File(zipPath);

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(zipPath);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 ServletOutputStream sos = response.getOutputStream();
                 BufferedOutputStream bos = new BufferedOutputStream(sos)) {
                // 设置响应头
                response.setContentType("application/zip");
                response.setHeader(Header.CONTENT_DISPOSITION.toString(), "attachment;filename=" + zipFilename);
                int bytesRead;
                byte[] buffer = new byte[ZIP_BUFFER_SIZE];
                while ((bytesRead = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                bos.flush();
            } catch (IOException e) {
                log.error("File download error.", e);
            }
        }
    }

    /**
     * 删除文件
     *
     * @param files 待删除的文件
     */
    private static void deleteFile(List<File> files) {
        for (File file : files) {
            FileUtil.del(file);
        }
    }

}
