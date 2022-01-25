package com.haoo.iframe.util;

import lombok.extern.slf4j.Slf4j;

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
    public static void renameFileTo(String oldSource, String newSource, String name) throws IOException {
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
    public static void deleteFile(String source) {
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
    public static void dirsCopyTo(String oldSource, String newSource) {
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
                mkDirs(end.getParent());
                dirsCopy(start.getParent(), end.getParent(), start.getName());
            } else {
                for (String temp : filePath) {
                    dirsCopy(oldSource, newSource, temp);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private static void dirsCopy(String sourcePathDir, String newPathDir, String temp) throws IOException {
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
            dirsCopyTo(sourcePath, newPath);
        }
    }


    /**
     * 创建目录
     *
     * @param source
     */
    public static void mkDirs(String source) {
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
     * 指定每一份文件的边界写入不同的文件中
     *
     * @param file  源文件
     * @param index 文件顺序标识
     * @param begin 开始指针的位置
     * @param end   结束指针的位置
     * @return 文件指针
     */
    public static long getWrite(String file, int index, long begin, long end) {

        long endPointer = 0L;

        try {
            //声明切割文件磁盘空间
            RandomAccessFile in = new RandomAccessFile(new File(file), "r");
            //定义一个可读可写并且后缀名为.tmp二进制的临时文件
            RandomAccessFile out = new RandomAccessFile(file + "_" + index + ".tmp", "rw");
            //声明具体每一个文件字节数组为1024
            byte[] b = new byte[1024];
            //从指定文件读取字节流
            in.seek(begin);
            int n = 0;
            while (in.getFilePointer() <= end && (n = in.read(b)) != -1) {
                out.write(b, 0, n); //写入不同的二进制文件
            }
            //定义当前读取文件指针
            endPointer = in.getFilePointer();
            //关闭输入流
            in.close();
            //关闭输出流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return endPointer;
    }


    /**
     * 文件切片
     *
     * @param file  需要被切割的源文件
     * @param count 切割文件的个数
     */
    private static void splitFile(String file, int count) {
        try {
            //预分配文件占用磁盘空间“r”表示只读的方式“rw”支持文件随机读取和写入
            RandomAccessFile raf = new RandomAccessFile(new File(file), "r");
            //文件长度
            long length = raf.length();
            //计算切片后，每一文件的大小
            long maxSize = length / count;
            //定义初始文件偏移量（读取文件进度）
            long offset = 0L;
            //开始切割
            for (int i = 0; i < count - 1; i++) { //count-1 其中的一份文件不处理
                //初始化
                long fbegin = offset;
                //分割第几份文件
                long fend = (i + 1) * maxSize;
                //写入二进制临时文件中
                offset = getWrite(file, i, fbegin, fend);
            }
            //将剩余的写入到最后一份文件中
            if ((int) (length - offset) > 0) {
                getWrite(file, count - 1, offset, length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并文件
     *
     * @param file     指定合并后的文件
     * @param tempFile 分割前的文件名
     * @param temCount 文件的个数
     */
    public static void mergeSplitFile(String file, String tempFile, int temCount) {
        RandomAccessFile raf = null;
        try {
            // 声明随机可读可写的文件
            raf = new RandomAccessFile(new File(file), "rw");
            // 开始合并文件，对应切片的二进制文件
            for (int i = 0; i < temCount; i++) {
                RandomAccessFile reader = new RandomAccessFile(
                        new File(tempFile + "_" + i + ".tmp"), "r");
                byte[] b = new byte[1024];
                int n = 0;
                while ((n = reader.read(b)) != -1) {
                    raf.write(b, 0, n);
                }
                reader.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //方法说明
        showMethod();

        String file = "C:\\Users\\ccp-114\\Desktop\\ddd\\qqqqq.docx";
        int count = 8;
        int temCount = count;
        String tempFile = file;
//        splitFile(file, count);
        mergeSplitFile(file, tempFile, temCount);
        for (int i = 0; i < temCount; i++) {
            deleteFile(tempFile + "_" + i + ".tmp");
        }

    }

    public static void showMethod() {
        OtherUtil.println("文件切片:【splitFile】");
        OtherUtil.println("文件切片合并:【mergeSplitFile】");
        OtherUtil.println("移动文件并重命名:【renameFileTo】");
        OtherUtil.println("创建文件:【createFile】");
        OtherUtil.println("删除文件:【deleteFile】");
        OtherUtil.println("文件迁移至【目录】:【dirsCopyTo】");
        OtherUtil.println("创建目录:【mkDirs】");
        OtherUtil.println("删除文件夹及目录下所有文件:【removeDir】");
        OtherUtil.println("下载文件方法:【download】");
    }

}
