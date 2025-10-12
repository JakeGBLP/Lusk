package it.jakegblp.lusk.nms.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, 
         ElementType.CONSTRUCTOR, ElementType.PARAMETER})
public @interface Availability {
    String addedIn() default "";
    String removedIn() default "";
}