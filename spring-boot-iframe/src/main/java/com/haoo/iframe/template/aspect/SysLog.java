package com.haoo.iframe.template.aspect;

import java.lang.annotation.*;

/**
 * 系统日志
 * <p></p>
 *
 * @author pluto
 * @version 1.0
 * @createdate 2022/1/27 3:13 PM
 * @see SysLog
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
