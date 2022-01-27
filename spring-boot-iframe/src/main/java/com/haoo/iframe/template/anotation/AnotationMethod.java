package com.haoo.iframe.template.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnotationMethod {

    /**
     * default 后面跟默认值
     * @return
     */
    Class value() default Student.class;

}
