package com.thinking.machines.webrock.annotations;
import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnStartup{
public int priority() default -1;
}