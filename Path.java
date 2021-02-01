package com.thinking.machines.webrock.annotations;
import java.lang.annotation.*;
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Path{
public String value() default "";
}