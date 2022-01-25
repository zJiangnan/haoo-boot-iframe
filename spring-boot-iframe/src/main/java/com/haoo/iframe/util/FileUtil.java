package com.haoo.iframe.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

@Slf4j
public class FileUtil {

    /**
     * 在新的目录生成重命名文件
     *
     * @param oldSource
     * @param newSource
     * @param name
     */
    public static void copyToFolder(String oldSource, String newSource, String name) throws IOException {
        //源目标文件
        File oldFile = new File(oldSource);
        //目的路径
        File newFile = new File(newSource);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        //最终路径
        File endFile = new File(newFile + File.separator + name);
        if (endFile.exists()) {
            endFile.delete();
        }
        //复制
        try {
            Files.copy(oldFile.toPath(), endFile.toPath());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 创建文件
     *
     * @param source
     */
    public static void createFile(String source) {
        File file = new File(source);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("File fail:{}", e);
            }
        }

    }

    /**
     * 删除文件
     *
     * @param source
     */
    public static void delFile(String source) {
        File file = new File(source);
        //删除
        try {
            if (Files.deleteIfExists(file.toPath())) {
                Files.deleteIfExists(file.toPath());
            }
        } catch (IOException e) {
            log.error("File fail:{}", e);
        }
    }


    /**
     * 文件夹及子文件迁移
     *
     * @param oldSource
     * @param newSource
     * @throws IOException
     */
    public static void folderTransfer(String oldSource, String newSource) {
        try {

            File start = new File(oldSource);
            File end = new File(newSource);
            //获取该文件夹下的所有文件以及目录的名字
            String[] filePath = start.list();
            if (!end.exists()) {
                end.mkdir();
            }
            //是否是文件
            if (start.isFile()) {
                createFolder(end.getParent());
                fileCopy(start.getParent(), end.getParent(), start.getName());
            } else {
                for (String temp : filePath) {
                    fileCopy(oldSource, newSource, temp);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private static void fileCopy(String sourcePathDir, String newPathDir, String temp) throws IOException {
        //为文件则进行拷贝
        String sourcePath = sourcePathDir + File.separator + temp;
        String newPath = newPathDir + File.separator + temp;

        File oldFile = new File(sourcePath);
        File newFile = new File(newPath);
        if (!oldFile.isDirectory()) {
            FileInputStream fileInputStream = new FileInputStream(oldFile);
            //新文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            byte[] buffer = new byte[9048];
            int len;
            //将文件流信息读取文件缓存区，如果读取结果不为-1就代表文件没有读取完毕，反之已经读取完毕
            while ((len = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
                fileOutputStream.flush();
            }
            fileInputStream.close();
            fileOutputStream.close();
        } else {
            folderTransfer(sourcePath, newPath);
        }
    }


    /**
     * 创建目录
     *
     * @param source
     */
    public static void createFolder(String source) {
        File file = new File(source);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 删除文件及文件夹
     *
     * @param source
     */
    public static void removeDir(String source) {
        File file = new File(source);
        //判断是否为文件夹
        if (file.isDirectory()) {
            //获取该文件夹下的子文件夹
            File[] files = file.listFiles();
            //循环子文件夹重复调用delete方法
            for (int i = 0; i < files.length; i++) {
                removeDir(files[i].getPath());
            }
        }
        //若为空文件夹或者文件删除，File类的删除方法
        file.delete();
    }

    /**
     * 文件下载
     *
     * @param source
     * @param response
     */
    public static void download(String source, HttpServletResponse response) {
        File file = new File(source);
        if (!file.exists()) {
            log.error("file does not exist ");
        }
        String fileName = null;
        try {
            fileName = new String(file.getName().getBytes(), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error("fail file name to convert");
        }
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

        byte[] buffer = new byte[9048];
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            OutputStream os = response.getOutputStream();

            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            log.error("download fail:{}", e);
        }
    }


    /**
     * 随机key
     *
     * @return
     */
    public static String random(String code) {
        int random = (int) (Math.random() * 1000);
        String key = System.currentTimeMillis() + random + "";
        return StringUtils.isEmpty(code) ? key : code + key;
    }

    /**
     * 随机key
     *
     * @return
     */
    public static String random() {
        int random = (int) (Math.random() * 1000);
        String key = System.currentTimeMillis() + random + "";
        return key;
    }

}
