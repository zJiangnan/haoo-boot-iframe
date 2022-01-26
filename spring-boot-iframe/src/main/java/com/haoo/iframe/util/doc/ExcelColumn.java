package com.haoo.iframe.util.doc;

import java.lang.annotation.*;

/**
 * @Package: cn.echo.enterprise.utils
 * @Author: pluto
 * @CreateTime: 2021/10/26 5:37 下午
 * @Description: 实体类所需bean(Excel属性标题 、 位置等)
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    /**
     * Excel标题名
     *
     * @return
     * @author Lynch
     */
    String value() default "";

    /**
     * Excel从左往右排列位置
     *
     * @return
     * @author Lynch
     */
    int col() default 0;
}
