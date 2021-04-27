package de.yniklas.packagerize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Include {
    String key() default "";
    String[] scopes() default {};
}
