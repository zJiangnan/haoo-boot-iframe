package com.haoo.iframe.util;

import com.haoo.iframe.common.constant.Constants;
import com.haoo.iframe.common.enums.ApiCode;
import com.haoo.iframe.common.exception.BizException;
import com.haoo.iframe.common.system.UploadParameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Slf4j
public class FileUtils {

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
    public static File createFile(String source) {
        File file = new File(source);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("File fail:{}", e);
            }
        }
        return file;
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

    /***
     * 上传文件
     * @param file 要上传的文件
     * @param param 具体
     */
    public static void uploadFile(File file, UploadParameter param) {
        FileInputStream fis = null;
        FileInputStream fisNew = null;
        RandomAccessFile out = null;
        try {
            //声明可读文件
            RandomAccessFile in = new RandomAccessFile(file, Constants.RANDOM_ACCESS_FILE_R);

            // 生成文件
            File newFile = new File(param.getSavePath());
            if (newFile.exists()) {
                fis = new FileInputStream(file);
                fisNew = new FileInputStream(newFile);
                //fis.available() 字节
                //校验MD5数字签名，判断是否同一文件（上传文件与本地文件是否相等）
                if (DigestUtils.md5Hex(fis).equals(DigestUtils.md5Hex(fisNew))) return;

                in.seek(param.getPos());
                // 声明可读写文件
                out = new RandomAccessFile(newFile, Constants.RANDOM_ACCESS_FILE_RW);
                // 跳转指针
                out.seek(param.getPos());
            } else {
                //创建文件夹
                mkDirs(newFile.getParent());
                out = new RandomAccessFile(newFile, Constants.RANDOM_ACCESS_FILE_RW);
            }
            Constants.MAP.put(newFile.getName(), true);


            //定义byte基数
            byte[] bytes = new byte[9048];
            int len;
            log.info("Start downloading....{}", System.currentTimeMillis());
            while ((len = in.read(bytes)) != -1 && Constants.MAP.get(newFile.getName())) {
                out.write(bytes, 0, len);
            }
            log.warn("file pos:{}", in.getFilePointer());
            log.info("Stop downloading....{}", System.currentTimeMillis());
            //关闭流
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null && fisNew != null) {
                    fis.close();
                    fisNew.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //删除临时文件temp
            file.delete();
        }

    }


    private static synchronized File getFile(UploadParameter param) {
        //定义文件
        File newFile = new File(param.getSavePath());
        //是否存在:存在名称后面叠加_copy
        if (newFile.exists()) {
            StringBuffer buffer = new StringBuffer();
            //文件路径
            String filePath = newFile.getParent() + File.separator;
            //文件名称
            String fileName = newFile.getName().substring(0, newFile.getName().lastIndexOf("."));
            //文件后缀
            String suffix = newFile.getName().substring(newFile.getName().lastIndexOf("."));
            buffer.append(filePath);
            buffer.append(fileName);
            buffer.append(Constants.FILE_UTIL_SUFFIX_COPY);
            buffer.append(suffix);
            String name = buffer.toString();
            //递归
            param.setSavePath(name);
            return getFile(param);
        }
        Constants.MAP.put(newFile.getName(), true);
        log.info("last name:{}", newFile.getName());
        return newFile;
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
            RandomAccessFile in = new RandomAccessFile(new File(file), Constants.RANDOM_ACCESS_FILE_R);
            //定义一个可读可写并且后缀名为.tmp二进制的临时文件
            RandomAccessFile out = new RandomAccessFile(file + "_" + index + ".tmp", Constants.RANDOM_ACCESS_FILE_RW);
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
            RandomAccessFile raf = new RandomAccessFile(new File(file), Constants.RANDOM_ACCESS_FILE_R);
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
            raf = new RandomAccessFile(new File(file), Constants.RANDOM_ACCESS_FILE_RW);
            // 开始合并文件，对应切片的二进制文件
            for (int i = 0; i < temCount; i++) {
                RandomAccessFile reader = new RandomAccessFile(
                        new File(tempFile + "_" + i + ".tmp"), Constants.RANDOM_ACCESS_FILE_R);
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


    /**
     * 功能:压缩多个文件成一个zip文件
     *
     * @param srcfile：源文件列表
     * @param zipfile：压缩后的文件
     */
    public static void zipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            log.info("压缩完成.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能:解压缩
     *
     * @param zipfile：需要解压缩的文件
     * @param descDir：解压后的目标目录
     */
    public static void unZipFiles(File zipfile, String descDir) {
        try {
            ZipFile zf = new ZipFile(zipfile);
            for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zf.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + zipEntryName);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
                log.info("解压缩完成.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将 MultipartFile 类型文件流转为 File 类型
     *
     * @param multipartFile
     * @return
     */
    public static File fileVMultipartFile(MultipartFile multipartFile) {
//        选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file = File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            //大坑 file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 将 File 类型文件流转为 MultipartFile 类型
     *
     * @param path     路径
     * @param suffix   文件类型
     * @param fileName 文件名
     * @return
     */
    public static MultipartFile multipartFileVFile(String path, String suffix, String fileName) {
        File file = new File(path + "/" + fileName);
        MultipartFile mulFile = null;
        try {
            mulFile = new MockMultipartFile(
                    fileName, //文件名
                    fileName, //originalFileName 相当于上传文件在客户机上的文件名
                    suffix, //文件类型
                    new FileInputStream(file) //文件流
            );
        } catch (Exception e) {
            throw new BizException(ApiCode.FAIL.getCode(), "文件转换失败");
        }
        return mulFile;
    }

    /**
     * 将 Workbook 转 MultipartFile 类型
     *
     * @param workbook Excel
     * @param fileName 文件名
     * @param fileType 文件后缀；文件类型
     * @return
     * @throws IOException
     */
    public static MultipartFile workbookFile(Workbook workbook, String fileName, String fileType) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] barray = bos.toByteArray();
        InputStream is = new ByteArrayInputStream(barray);
        MultipartFile mf = new MockMultipartFile(fileName, fileName, fileType, is);
        return mf;
    }

    public static void showMethod() {
        OtherUtils.println("文件切片:【splitFile】");
        OtherUtils.println("文件切片合并:【mergeSplitFile】");
        OtherUtils.println("移动文件并重命名:【renameFileTo】");
        OtherUtils.println("创建文件:【createFile】");
        OtherUtils.println("删除文件:【deleteFile】");
        OtherUtils.println("文件迁移至【目录】:【dirsCopyTo】");
        OtherUtils.println("创建目录:【mkDirs】");
        OtherUtils.println("删除文件夹及目录下所有文件:【removeDir】");
        OtherUtils.println("下载文件方法:【download】");
        OtherUtils.println("上传文件方法:【uploadFile】");
        OtherUtils.println("压缩文件:【zipFiles】");
        OtherUtils.println("解压文件:【unZipFiles】");
        OtherUtils.println("将 MultipartFile 类型文件流转为 File 类型:【fileVMultipartFile】");
        OtherUtils.println("将 File 类型文件流转为 MultipartFile 类型:【multipartFileVFile】");
        OtherUtils.println("将 Workbook 转 MultipartFile 类型:【workbookFile】");
    }


    public static void main(String[] args) throws IOException {
        //方法说明
//        showMethod();
//
//        String file = "C:\\Users\\ccp-114\\Desktop\\ddd\\eeeeee.docx";
//        int count = 8;
//        int temCount = count;
//        String tempFile = file;
//        splitFile(file, count);
        //mergeSplitFile(file, tempFile, temCount);


    }


}
