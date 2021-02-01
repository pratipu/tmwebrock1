package com.thinking.machines.webrock.annotations;
import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface AutoWired{
public String name() default "";
public String setter() default "";
public String getter() default "";
}