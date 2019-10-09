package com.my.test.demo.annotation;

import java.lang.annotation.*;

/**
 * 是否脱敏注解，作用于方法上（ElementType.METHOD），类或者接口上（ElementType.TYPE）
 * 注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在（RetentionPolicy.RUNTIME）
 * @Documented 注解表明这个注释是由 javadoc记录的
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {

    /**
     * 是否脱敏
     *
     * @return 是否需要脱敏
     */
    boolean value() default true;
}
