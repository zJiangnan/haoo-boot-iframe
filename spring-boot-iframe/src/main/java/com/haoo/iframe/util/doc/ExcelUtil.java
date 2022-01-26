package com.haoo.iframe.util.doc;

import com.haoo.iframe.errcode.ApiCode;
import com.haoo.iframe.errcode.BizException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Package: cn.echo.enterprise.utils
 * @Author: pluto
 * @CreateTime: 2021/10/26 5:35 下午
 * @Description: Excel操作工具类
 **/
public class ExcelUtil {

    private final static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    private final static String EXCEL2003 = "xls";
    private final static String EXCEL2007 = "xlsx";

    private final static int MAX_ROW_NUM = 5000;

    /**
     * 创建Workbook
     *
     * @param filename 文件名
     * @param is       输入流
     * @return
     */
    public static Workbook getWorkbook(String filename, InputStream is) throws IOException {
        if (filename.endsWith(EXCEL2007)) {
            return new XSSFWorkbook(is);
        }
        if (filename.endsWith(EXCEL2003)) {
            return new HSSFWorkbook(is);
        }
        throw new BizException(ApiCode.FAIL.getCode(), "请上传excel文件");
    }

    /**
     * 从excel中读取数据
     *
     * @param sheet
     * @param cls
     * @param <T>
     * @param ignoreLines
     * @return
     */
    public static <T> List<T> readExcel(Sheet sheet, Class<T> cls, int ignoreLines) {
        List<T> dataList = new ArrayList<>();
        //类映射  注解 value-->bean columns
        Map<String, List<Field>> classMap = new HashMap<>(16);
        List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
        fields.forEach(
                field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        String value = annotation.value();
                        if (StringUtils.isBlank(value)) {
                            return;//return起到的作用和continue是相同的 语法
                        }
                        if (!classMap.containsKey(value)) {
                            classMap.put(value, new ArrayList<>());
                        }
                        field.setAccessible(true);
                        classMap.get(value).add(field);
                    }
                }
        );
        //索引-->columns
        Map<Integer, List<Field>> reflectionMap = new HashMap<>(16);
        //默认读取第一个sheet
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
        if (physicalNumberOfRows > MAX_ROW_NUM) {
            throw new BizException(ApiCode.FAIL.getCode(), "上传的Excel超过" + MAX_ROW_NUM + "行");
        }
        boolean firstRow = true;
        for (int i = sheet.getFirstRowNum() + ignoreLines; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            //首行提取注解
            if (firstRow) {
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    String cellValue = getCellValue(cell);
                    if (classMap.containsKey(cellValue)) {
                        reflectionMap.put(j, classMap.get(cellValue));
                    }
                }
                firstRow = false;
            } else {
                //忽略空白行
                if (row == null) {
                    continue;
                }
                try {
                    T t = cls.newInstance();
                    //判断是否为空白行
                    boolean allBlank = true;
                    for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                        if (reflectionMap.containsKey(j)) {
                            Cell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);
                            if (StringUtils.isNotBlank(cellValue)) {
                                allBlank = false;
                            }
                            List<Field> fieldList = reflectionMap.get(j);
                            fieldList.forEach(
                                    x -> {
                                        try {
                                            handleField(t, cellValue, x);
                                        } catch (Exception e) {
                                            log.error(String.format("reflect field:%s value:%s exception!", x.getName(), cellValue), e);
                                        }
                                    }
                            );
                        }
                    }
                    if (!allBlank) {
                        dataList.add(t);
                    } else {
                        log.warn(String.format("row:%s is blank ignore!", i));
                    }
                } catch (Exception e) {
                    log.error(String.format("parse row:%s exception!", i), e);
                    throw new BizException(ApiCode.FAIL.getCode(), "解析行数据失败：" + e.getMessage());
                }
            }
        }
        return dataList;
    }

    public static <T> List<T> readExcel(String path, Class<T> cls, MultipartFile file) {

        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            log.error("上传文件格式不正确");
        }
        List<T> dataList = new ArrayList<>();
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();
            if (fileName.endsWith(EXCEL2007)) {
                workbook = new XSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2003)) {
                workbook = new HSSFWorkbook(is);
            }
            if (workbook != null) {
                //默认读取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);
                return readExcel(sheet, cls, 0);
            }
        } catch (Exception e) {
            log.error(String.format("parse excel exception!"), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error(String.format("parse excel exception!"), e);
                }
            }
        }
        return dataList;
    }

    private static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();
        if (type == null || type == void.class || StringUtils.isBlank(value)) {
            return;
        }
        if (type == Object.class) {
            field.set(t, value);
            //数字类型
        } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            Date date = null;
            try {
                date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(value);
            } catch (ParseException e) {
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
                } catch (ParseException e1) {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
                }
            }
            field.set(t, date);
        } else if (type == String.class) {
            field.set(t, value);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        String cellValue = "";
        // 以下是判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC: // 数字
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                } else {
                    DataFormatter dataFormatter = new DataFormatter();
                    cellValue = dataFormatter.formatCellValue(cell);
                }
                break;
            case STRING: // 字符串
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN: // Boolean
                cellValue = cell.getBooleanCellValue() + "";
                break;
            case FORMULA: // 公式
                cellValue = cell.getCellFormula() + "";
                break;
            case BLANK: // 空值
                cellValue = "";
                break;
            case ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;

    }

    /**
     * 浏览器下载excel
     *
     * @param fileName
     * @param wb
     * @param response
     */
    private static void buildExcelDocument(String fileName, Workbook wb, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.flushBuffer();
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成excel文件
     * @param path 生成excel路径
     * @param wb Workbook对象
     */
    private static void buildExcelFile(String path, Workbook wb) {

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            wb.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> void writeExcel(HttpServletResponse response, List<T> dataList, Class<T> cls, String fileName) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null && annotation.col() > 0) {
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        col = annotation.col();
                    }
                    return col;
                })).collect(Collectors.toList());

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            //写入头部
            fieldList.forEach(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                }
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                Font font = wb.createFont();
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
            });
        }
        if (CollectionUtils.isNotEmpty(dataList)) {
            dataList.forEach(t -> {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {
                    Class<?> type = field.getType();
                    Object value = "";
                    try {
                        value = field.get(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    if (value != null) {
                        if (type == Date.class) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                        cell.setCellValue(value.toString());
                    }
                });
            });
        }
        //冻结窗格
        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
        //浏览器下载excel
        buildExcelDocument(fileName, wb, response);
        //生成excel文件
        //buildExcelFile(fileName,wb);
    }

    /*public static void wrieExcel(HttpServletResponse response, List<?> list, String sheetName) throws IOException {
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();

        //定义工作表对象
        WriteSheet sheet = EasyExcel.writerSheet(0, sheetName).head(表头).build();

        //往excel文件中写入数据
        excelWriter.write(list, sheet);
        //关闭输出流
        excelWriter.finish();

    }*/

}
