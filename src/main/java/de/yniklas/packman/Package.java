package de.yniklas.packman;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates, that all (non-excluded and non-include-only) fields are packed in the objects package.
 * So, fields must not be included explicitly by an @Include tag.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Package {
    /**
     * Defines the key of the packed object in a multi-object package.
     * Without explicitly declaring the key in the annotations parameters (@Package(key = "dummy")),
     * the class name will be used as key in the multi-object package.
     *
     * Single-Object packages are not effected by any key value.
     */
    String key() default "";

    /**
     * Defines the scopes on which the class objects are being packaged.
     * Default/{} means all scopes, so on every called scope, an object of this class will be packaged.
     */
    String[] scopes() default {};
}
