package com.thinking.machines.webrock.annotations;
import  java.lang.annotation.*;
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)

public @interface RequestParameter{
public String value() default "";
}