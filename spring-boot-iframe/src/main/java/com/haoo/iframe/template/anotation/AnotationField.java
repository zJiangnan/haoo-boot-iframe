package com.haoo.iframe.template.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnotationField {

    /**
     * default 后面跟默认值
     * @return
     */
    String value() default "";

    String index() default "";

}
