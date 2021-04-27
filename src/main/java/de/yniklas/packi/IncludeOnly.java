package de.yniklas.packi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Overwrites the access level of @Package.
 * Weaker than excludes.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IncludeOnly {
    String key() default "";
    String[] scopes() default {};
}
