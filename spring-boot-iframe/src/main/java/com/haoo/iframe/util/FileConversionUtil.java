package cn.echo.enterprise.utils;

import cn.echo.enterprise.errcode.ApiCode;
import cn.echo.enterprise.errcode.BizException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @Package: cn.echo.enterprise.utils
 * @Author: pluto
 * @CreateTime: 2021/10/26 4:30 下午
 * @Description: File和MultipartFile之间转换
 * @since 1.0
 **/
public class FileConversion {

    /**
     * 将 MultipartFile 类型文件流转为 File 类型
     * @param multipartFile
     * @return
     */
    public static File fileVMultipartFile(MultipartFile multipartFile) {
//        选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split(".");
            file=File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 将 File 类型文件流转为 MultipartFile 类型
     * @param path 路径
     * @param suffix 文件类型
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
        InputStream is =new ByteArrayInputStream(barray);
        MultipartFile mf = new MockMultipartFile(fileName,fileName,fileType,is);
        return mf;
    }

}
