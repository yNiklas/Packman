package de.yniklas.packi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Overwrites the access level of @Package.
 * Weaker than excludes.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IncludeOnly {
    String key() default "";
    String[] scopes() default {};
}
