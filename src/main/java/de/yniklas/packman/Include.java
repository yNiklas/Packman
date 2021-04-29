package de.yniklas.packman;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a field being included in packages.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Include {
    /**
     * Defines the key of the field in every package.
     */
    String key() default "";

    /**
     * Defines the scopes, where the field is included in packages.
     * Default/{} means, the field is included in every package regardless of the scope.
     */
    String[] scopes() default {};
}
