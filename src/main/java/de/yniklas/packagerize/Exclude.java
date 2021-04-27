package de.yniklas.packagerize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {
    String[] scopes() default {};
}
